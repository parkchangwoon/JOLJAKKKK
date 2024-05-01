package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    // 글 작성
    public void write(Board board) {
        // 현재 점포의 게시글 수를 조회하여 storeSpecificId 설정
        List<Board> storeBoards = boardRepository.findByStore(board.getStore());
        int storeSpecificId = storeBoards.size() + 1;

        // storeSpecificId를 동적으로 설정하고 저장 (DB에는 저장되지 않음)
        board.setContent("[글 번호: " + storeSpecificId + "] " + board.getContent());

        // 게시글을 저장할 때 해당 점포 정보를 함께 저장
        boardRepository.save(board);
    }

    public Page<Board> boardListByStore(String store, Pageable pageable) {
        return boardRepository.findByStore(store, pageable);
    }

    // 게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public Page<Board> boardSeachList(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    public Page<Board> boardListByStoreAndTitleContaining(String store, String title, Pageable pageable) {
        return boardRepository.findByStoreAndTitleContaining(store, title, pageable);
    }

    // 특정 게시글 불러오기
    public Board boardview(Integer id) {
        return boardRepository.findById(id).orElse(null);
    }

    // 별점 업데이트
    public void rateBoard(Integer id, int rating) {
        Board board = boardRepository.findById(id).orElse(null);
        if (board != null) {
            board.setRating(rating);
            boardRepository.save(board);
        }
    }

    // 게시글 수정
    public void boardUpdate(Integer id, String title, String content, Integer rating) {
        Board board = boardRepository.findById(id).orElse(null);
        if (board != null) {
            board.setTitle(title);
            board.setContent(content);
            board.setRating(rating);
            boardRepository.save(board);
        }
    }
}