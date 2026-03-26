package com.ruoyi.system.recommend.chain;

import com.ruoyi.system.recommend.domain.PostCandidate;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RecallResult {
    private List<PostCandidate> candidates = new ArrayList<>();
}