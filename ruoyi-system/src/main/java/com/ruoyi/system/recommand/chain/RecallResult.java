package com.ruoyi.system.recommand.chain;

import com.ruoyi.system.recommand.domain.PostCandidate;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RecallResult {
    private List<PostCandidate> candidates = new ArrayList<>();
}