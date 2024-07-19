package org.example.jiaoji;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.example.jiaoji.mapper.ObjectMapper;
import org.example.jiaoji.mapper.TopicMapper;
import org.example.jiaoji.mapper.UserMapper;
import org.example.jiaoji.pojo.Topic;
import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.pojo.User;
import org.example.jiaoji.service.ObjectService;
import org.example.jiaoji.service.TopicService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class indexTest {
    @Autowired
    private TopicService topicService;
    @Autowired
    private ObjectService objectService;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserMapper userMapper;

    private RestHighLevelClient client;

//    @Test
//    void bulk() throws IOException {
//        List<Objects> objects = objectMapper.selectAll();
//        BulkRequest request = new BulkRequest();
//        for(Objects object : objects) {
//            request.add(new IndexRequest("object")
//                    .id(object.getId().toString())
//                    .source(JSON.toJSONString(object), XContentType.JSON));
//        }
//        client.bulk(request, RequestOptions.DEFAULT);
//    }

    @Test
    void bulk() throws IOException {
        List<Topic> topics = topicMapper.selectAll();
        BulkRequest request = new BulkRequest();
        for(Topic topic : topics) {
            request.add(new IndexRequest("topic")
                    .id(topic.getId().toString())
                    .source(JSON.toJSONString(topic), XContentType.JSON));
        }
        client.bulk(request, RequestOptions.DEFAULT);
    }

//    @Test
//    void bulk() throws IOException {
//        List<User> users = userMapper.selectAll();
//        BulkRequest request = new BulkRequest();
//        for(User user : users) {
//            request.add(new IndexRequest("user")
//                    .id(user.getId().toString())
//                    .source(JSON.toJSONString(user), XContentType.JSON));
//        }
//        client.bulk(request, RequestOptions.DEFAULT);
//    }

    @Test
    void searchDocument() throws IOException {
        SearchRequest request = new SearchRequest("topic");
        // 组织DSL参数
        request.source()
                .query(QueryBuilders.matchAllQuery());
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        for(SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            Topic topic = JSON.parseObject(json, Topic.class);
            System.out.println(topic);
        }
    }

    @Test
    void finalSearch() throws IOException {
        SearchRequest request = new SearchRequest("topic");
        // 组织DSL参数
        request.source()
                .query(QueryBuilders.multiMatchQuery("书推荐", "title", "introduction"));
        request.source().from(0).size(1);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        for(SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            Topic topic = JSON.parseObject(json, Topic.class);
            System.out.println(topic);
        }
    }


    @Test
    void addDocument() throws IOException {
        Topic topic = topicMapper.selectById(1);
        //转换为Doc数据
        // 1.准备Request请求
        IndexRequest request = new IndexRequest("topic").id("1");

        // 2.准备Json文档
        request.source(JSON.toJSONString(topic), XContentType.JSON);
        // 3.发送请求
        client.index(request, RequestOptions.DEFAULT);
    }

    @Test
    void getDocument() throws IOException {
        GetRequest request = new GetRequest("user", "7");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        String json = response.getSourceAsString();
        User user = JSON.parseObject(json, User.class);
        System.out.println(user);
    }

    @Test
    void updateDocument() throws IOException {
        UpdateRequest request = new UpdateRequest("topic", "1");
        request.doc(
                "price", "952",
                "starNum", "四钻"
        );
        client.update(request, RequestOptions.DEFAULT);
    }

    @Test
    void deleteDocument() throws IOException {
        DeleteRequest request = new DeleteRequest("topic", "1");
        client.delete(request, RequestOptions.DEFAULT);
    }

    @Test
    void deleteHotelIndex() throws IOException {
        // 1.创建Request对象
        DeleteIndexRequest request = new DeleteIndexRequest("topic");
        // 2.发起请求
        client.indices().delete(request, RequestOptions.DEFAULT);
    }

    @Test
    void existsHotelIndex() throws IOException {
        // 1.创建Request对象
        GetIndexRequest request = new GetIndexRequest("topic");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists?"索引库已存在":"索引库不存在");
    }

    @BeforeEach
    void setUp() throws Exception {
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://124.70.209.108:9200")
        ));
    }

    @AfterEach
    void tearDown() throws Exception {
        client.close();
    }
}
