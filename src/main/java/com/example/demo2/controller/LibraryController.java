package com.example.demo2.controller;

import com.example.demo2.dto.RequestIdDto;
import com.example.demo2.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/library")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;

//    public LibraryController(LibraryService libraryService) {
//        this.libraryService = libraryService;
//    }

    @GetMapping("/book")
    public ResponseEntity readBooks (@RequestParam(value = "isbn", required = false) String isbn) {
        if (isbn == null) {
            return ResponseEntity.ok(libraryService.readBooks());
        }
        return ResponseEntity.ok(libraryService.readBook(isbn));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<RequestIdDto> readBook (@PathVariable("bookId") Long bookId) {
        return ResponseEntity.ok(libraryService.readBook(bookId));
    }
}
