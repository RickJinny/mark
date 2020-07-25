package com.rickjinny.mark.controller.p17_oom.t01_UserNameAutoComplete;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Slf4j
@Service
public class UserNameAutoCompleteService {

    // 自动完成的索引，key是用户输入的部分用户名，value是对应的用户数据
    private ConcurrentHashMap<String, List<UserDTO>> autoCompleteIndex = new ConcurrentHashMap<>();

    @Autowired
    private UserRepository userRepository;

    /**
     * 在介绍集合时，对于这种快速检索的需求，最好使用 Map 来实现，会比直接从 List 搜索快得多。
     * 为了实现这个功能，需要一个 HashMap 来存放这些用户数据，key是用户姓名索引，value是索引下对应的用户列表。
     * 举个例子：如果有两个用户 aa 和 bb, 那么 key 就有三个，分别是 a、aa 和 ab。用户输入字母 a 时，就能从 Value 这个
     * List 中拿到所有字母 a 开头的用户，即 aa 和 ab。
     *
     * 在数据库中存入 1万个测试用户，用户名由 a~j 这6个字母随机构成，然后把每一个用户名的前 1 个字母、前 2 个字母依次类推直到完整用户名作为 key 存入
     * 缓存中，缓存的 value 是一个 UserDTO 的 List，存放的是所有相同的用户名索引，以及对应的用户信息。
     */
    @PostConstruct
    public void wrong() {
        // 先保存 10000 个用户名随机的用户到数据库中
        userRepository.saveAll(LongStream.rangeClosed(1, 10000).mapToObj(i ->
                new UserEntity(i, randomName())).collect(Collectors.toList()));

        // 从数据库加载所有用户
        userRepository.findAll().forEach(userEntity -> {
            int length = userEntity.getName().length();
            // 对于每一个用户，对其用户名的前 N 位进行索引，N 可能是 1~6 六种长度类型
            for (int i = 0; i < length; i++) {
                String key = userEntity.getName().substring(0, i + 1);
                autoCompleteIndex.computeIfAbsent(key, s -> new ArrayList<>())
                        .add(new UserDTO(userEntity.getName()));
            }
        });
        log.info("autoCompleteIndex size:{} count:{}", autoCompleteIndex.size(),
                autoCompleteIndex.entrySet().stream().map(item -> item.getValue().size()).reduce(0, Integer::sum));
    }

    /**
     * 随机生成长度为 6 的英文名称，字母包含 abcdefgij
     */
    private String randomName() {
        return String.valueOf(Character.toChars(ThreadLocalRandom.current().nextInt(10) + 'a')).toUpperCase() +
                String.valueOf(Character.toChars(ThreadLocalRandom.current().nextInt(10) + 'a')) +
                String.valueOf(Character.toChars(ThreadLocalRandom.current().nextInt(10) + 'a')) +
                String.valueOf(Character.toChars(ThreadLocalRandom.current().nextInt(10) + 'a')) +
                String.valueOf(Character.toChars(ThreadLocalRandom.current().nextInt(10) + 'a')) +
                String.valueOf(Character.toChars(ThreadLocalRandom.current().nextInt(10) + 'a'));
    }
}
