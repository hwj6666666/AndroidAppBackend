package org.example.jiaoji.controller;


import com.github.pagehelper.PageInfo;
import org.example.jiaoji.pojo.Comment;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class CommentController {
    @Autowired
    private CommentService commentService;

    //takes 8s
    @GetMapping("/comments/get/{remarkId}")
    @ResponseBody
    public ResponseEntity<PageInfo<Comment>> getComment(@PathVariable Integer remarkId, @RequestParam Integer pageIndex,
                                                        @RequestParam Integer pageSize) {
        return ResponseEntity.ok(commentService.SelectByRemark(remarkId, pageSize, pageIndex));
    }

    
    @PostMapping("/comments")
    public Integer insert(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }

    @GetMapping("/comments/delete/{id}")
    public RetType delete(@PathVariable Integer id) {
        return commentService.deleteById(id);
    }

}
