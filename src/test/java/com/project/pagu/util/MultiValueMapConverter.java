package com.project.pagu.util;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Created by IntelliJ IDEA
 * User: hojun
 * Date: 2021-04-04 Time: 오전 12:41
 */
public class MultiValueMapConverter {

    private MultiValueMapConverter() {}

    public static MultiValueMap<String, String> convert(ObjectMapper objectMapper, Object dto) {
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            Map<String, String> map = objectMapper.convertValue(dto, new TypeReference<>() {});
            params.setAll(map);

            return params;
        } catch (Exception e) {
            throw new IllegalStateException("Url Parameter 변환중 오류가 발생했습니다.");
        }
    }
}