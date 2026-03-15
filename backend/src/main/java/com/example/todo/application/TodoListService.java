package com.example.todo.application;

import com.example.todo.domain.model.TodoItem;
import com.example.todo.domain.model.TodoList;
import com.example.todo.domain.repository.TodoItemRepository;
import com.example.todo.domain.repository.TodoListRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoListService {

  private final TodoListRepository todoListRepository;
  private final TodoItemRepository todoItemRepository;

  public TodoListService(
      TodoListRepository todoListRepository, TodoItemRepository todoItemRepository) {
    this.todoListRepository = todoListRepository;
    this.todoItemRepository = todoItemRepository;
  }

  @Transactional(readOnly = true)
  public List<TodoListWithItems> findAll() {
    List<TodoList> lists = todoListRepository.findAllOrderById();
    Map<Long, List<TodoItem>> itemsByList =
        todoItemRepository.findAllOrderByListIdAndId().stream()
            .collect(
                Collectors.groupingBy(TodoItem::listId, LinkedHashMap::new, Collectors.toList()));
    return lists.stream()
        .map(
            list ->
                new TodoListWithItems(
                    list.id(), list.title(), itemsByList.getOrDefault(list.id(), List.of())))
        .toList();
  }

  @Transactional
  public TodoListWithItems create(String title) {
    TodoList saved = todoListRepository.save(new TodoList(null, title));
    return new TodoListWithItems(saved.id(), saved.title(), List.of());
  }

  @Transactional
  public TodoListWithItems update(Long id, String title) {
    todoListRepository.findById(id).orElseThrow(() -> new TodoListNotFoundException(id));
    TodoList saved = todoListRepository.save(new TodoList(id, title));
    return new TodoListWithItems(
        saved.id(), saved.title(), todoItemRepository.findAllByListIdOrderById(id));
  }

  @Transactional
  public void delete(Long id) {
    todoListRepository.findById(id).orElseThrow(() -> new TodoListNotFoundException(id));
    todoItemRepository.deleteByListId(id);
    todoListRepository.deleteById(id);
  }
}
