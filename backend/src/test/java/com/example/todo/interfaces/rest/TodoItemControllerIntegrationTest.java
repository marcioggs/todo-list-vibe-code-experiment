package com.example.todo.interfaces.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.todo.infrastructure.persistence.TodoItemSpringDataRepository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TodoItemControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TodoItemSpringDataRepository repository;

  @Test
  void shouldCreateUpdateListAndDeleteTodoItems() throws Exception {
    repository.deleteAll();

    String createResponse = mockMvc.perform(post("/api/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"text":"Buy milk"}
                """))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    TodoItemResponse createdItem = objectMapper.readValue(createResponse, TodoItemResponse.class);
    assertThat(createdItem.id()).isNotNull();
    assertThat(createdItem.text()).isEqualTo("Buy milk");

    String listResponse = mockMvc.perform(get("/api/todos"))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    List<TodoItemResponse> listedItems =
        objectMapper.readValue(listResponse, new TypeReference<>() {});
    assertThat(listedItems).extracting(TodoItemResponse::text).containsExactly("Buy milk");

    String updateResponse = mockMvc.perform(put("/api/todos/{id}", createdItem.id())
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"text":"Buy oat milk"}
                """))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    TodoItemResponse updatedItem = objectMapper.readValue(updateResponse, TodoItemResponse.class);
    assertThat(updatedItem.text()).isEqualTo("Buy oat milk");

    mockMvc.perform(delete("/api/todos/{id}", createdItem.id()))
        .andExpect(status().isOk());

    assertThat(repository.count()).isZero();
  }

  @Test
  void shouldReturnNotFoundForUnknownTodo() throws Exception {
    String errorResponse = mockMvc.perform(delete("/api/todos/{id}", 999))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Map<String, String> error = objectMapper.readValue(errorResponse, new TypeReference<>() {});
    assertThat(error).containsEntry("message", "Todo item 999 was not found");
  }
}
