package org.solteh.web.controller.restws;

import org.attoparser.dom.INode;
import org.solteh.model.Node;
import org.solteh.service.INodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class NodeWS {
    @Autowired
    private INodeService nodeService;

    @GetMapping("nodes")
    @ResponseBody
    public List<Node> getAllNodes() {
        return nodeService.getAll();
    }
}
