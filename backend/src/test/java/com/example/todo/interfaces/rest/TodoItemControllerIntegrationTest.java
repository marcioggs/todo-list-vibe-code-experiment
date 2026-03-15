package com.example.todo.interfaces.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.todo.infrastructure.persistence.TodoItemSpringDataRepository;
import com.example.todo.infrastructure.persistence.TodoListSpringDataRepository;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class TodoItemControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private TodoItemSpringDataRepository repository;

  @Autowired private TodoListSpringDataRepository listRepository;

  @BeforeEach
  void cleanDatabase() {
    repository.deleteAll();
    listRepository.deleteAll();
  }

  private Long createList(String title) throws Exception {
    String body = "{\"title\":\"" + title + "\"}";
    String createResponse =
        mockMvc
            .perform(post("/api/lists").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    return objectMapper.readValue(createResponse, TodoListResponse.class).id();
  }

  @Test
  void shouldCreateUpdateListAndDeleteTodoItems() throws Exception {
    Long listId = createList("Groceries");

    String createResponse =
        mockMvc
            .perform(
                post("/api/lists/{listId}/todos", listId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"text\":\"Buy milk\"}"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    TodoItemResponse createdItem = objectMapper.readValue(createResponse, TodoItemResponse.class);
    assertThat(createdItem.id()).isNotNull();
    assertThat(createdItem.text()).isEqualTo("Buy milk");
    assertThat(createdItem.listId()).isEqualTo(listId);

    String listResponse =
        mockMvc
            .perform(get("/api/lists"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    List<TodoListResponse> listedLists =
        objectMapper.readValue(listResponse, new TypeReference<List<TodoListResponse>>() {});
    assertThat(listedLists)
        .singleElement()
        .satisfies(
            list ->
                assertThat(list.items())
                    .extracting(TodoItemResponse::text)
                    .containsExactly("Buy milk"));

    String updateResponse =
        mockMvc
            .perform(
                put("/api/lists/{listId}/todos/{id}", listId, createdItem.id())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"text\":\"Buy oat milk\"}"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    TodoItemResponse updatedItem = objectMapper.readValue(updateResponse, TodoItemResponse.class);
    assertThat(updatedItem.text()).isEqualTo("Buy oat milk");

    mockMvc
        .perform(delete("/api/lists/{listId}/todos/{id}", listId, createdItem.id()))
        .andExpect(status().isOk());

    assertThat(repository.count()).isZero();
  }

  @Test
  void shouldReturnNotFoundForUnknownTodo() throws Exception {
    Long listId = createList("Groceries");

    String errorResponse =
        mockMvc
            .perform(delete("/api/lists/{listId}/todos/{id}", listId, 999))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

    Map<String, String> error =
        objectMapper.readValue(errorResponse, new TypeReference<Map<String, String>>() {});
    assertThat(error).containsEntry("message", "Todo item 999 was not found");
  }
}
