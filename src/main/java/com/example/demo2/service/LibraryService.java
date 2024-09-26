package com.example.demo2.service;

import com.example.demo2.dto.RequestIdDto;
import com.example.demo2.dto.RequestIsbnDto;
import com.example.demo2.model.Book;
import com.example.demo2.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryService {
    private final BookRepository bookRepository;

    // Book 엔티티 받아서 BookDTO 객체로 반환
//    public BookDTO EntityToDTO(Book book) {
//        return new BookDTO(book.getId(), book.getName(), book.getIsbn());
//    }
//
//    // BookDTO 객체 받아서 Book 엔티티로 반환
//    // @Builder => 코드 간결하게
//    // 얘는 근데 왜 있어야 하지?
//    public Book DTOToEntity(BookDTO bookDTO) {
//        return Book.builder()
//                .id(bookDTO.getId())
//                .name(bookDTO.getName())
//                .isbn(bookDTO.getIsbn())
//                .build();
//    }

    public RequestIdDto readBook (Long id) { // pk 값으로 조회하는 api => name, isbn 반환
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            // builder => 객체 생성해서 그대로 반환
            return RequestIdDto.builder()
                    .name(book.get().getName())
                    .isbn(book.get().getIsbn())
                    .build();
        }
        throw new EntityNotFoundException("Can't find any book under given ID");
    }

    public List<RequestIdDto> readBooks() {
        // 모르겠다...
        // 빌더패턴 사용하게 되면 내가 필요한 값들만 반환 해 줄 수 있음
        // 빌더패턴 사용하면 생성자 매개변수 순서와 상관이 없다! (String name, String isbn) 이어도 isbn, name 순으로 줄 수 있음
        List<Book> books = bookRepository.findAll();
        List<RequestIdDto> bookDTO = books
                .stream()
                .map(book -> RequestIdDto.builder() // 스트림 내의 값 변환하여 새로운 스트림 생성 (빌더 호출해서 Book 객체 -> RequestIdDto로 변환 !!)
                        .name(book.getName())
                        .isbn(book.getIsbn())
                        .build())
                .collect(Collectors.toList()); // 스트림 요소들을 원하는 결과 형태로 수집
        return bookDTO;
    }

    public RequestIsbnDto readBook (String isbn) { // isbn으로 조회하는 api => id, name 반환
        Optional<Book> book = bookRepository.findByIsbn(isbn);
        if (book.isPresent()) {
            return RequestIsbnDto.builder()
                    .id(book.get().getId())
                    .name(book.get().getName())
                    .build();
        }
        throw new EntityNotFoundException("Can't find any book under given ISBN");
    }
}