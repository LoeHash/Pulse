package com.ruoyi.system.recommand.chain;

public interface RecallChain {
    RecallResult recall(RecallRequest request);
}