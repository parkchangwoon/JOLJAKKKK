package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/write")
    public String boardWriteForm(HttpSession session) {
        String selectedStore = (String) session.getAttribute("selectedStore");
        return selectedStore != null && !selectedStore.isEmpty() ? selectedStore : "";
    }

    @PostMapping("/writepro")
    public String boardWritePro(@RequestBody Board board) {
        if (board.getContent().isEmpty()) {
            return "내용을 입력해주세요.";
        } else {
            boardService.write(board);
            String encodedStore = UriUtils.encodeQueryParam(board.getStore(), StandardCharsets.UTF_8);
            return "redirect:/board/list?store=" + encodedStore;
        }
    }

    @GetMapping("/list")
    public Page<Board> boardlist(
            @RequestParam(name = "store", required = false) String store,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "searchKeyword", required = false) String searchKeyword,
            HttpSession session) {
        Page<Board> list = null;

        if (store != null && !store.isEmpty()) {
            list = boardService.boardListByStore(store, pageable);
        } else {
            String selectedStore = (String) session.getAttribute("selectedStore");
            if (selectedStore != null && !selectedStore.isEmpty()) {
                list = boardService.boardListByStore(selectedStore, pageable);
            } else {
                if (searchKeyword == null || searchKeyword.isEmpty()) {
                    list = boardService.boardList(pageable);
                } else {
                    list = boardService.boardSeachList(searchKeyword, pageable);
                }
            }
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startNumber = (nowPage - 1) * pageable.getPageSize() + 1;
        int counter = 0;
        for (Board board : list) {
            board.setDynamicId(startNumber + counter++);
        }

        return list;
    }

    @GetMapping("/view/{id}")
    public Board boardView(@PathVariable Integer id) {
        return boardService.boardview(id);
    }

    @PostMapping("/rate/{id}")
    public String rateBoard(@PathVariable("id") Integer id, @RequestParam("rate") int rating) {
        boardService.rateBoard(id, rating);
        return "Rating updated successfully";
    }

    @PostMapping("/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id,
                              @RequestParam("title") String title,
                              @RequestParam("content") String content,
                              @RequestParam("rating") Integer rating) {
        boardService.boardUpdate(id, title, content, rating);
        return "Board updated successfully";
    }
}