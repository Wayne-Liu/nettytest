package com.jdcloud.sdk.server.contract.service;

import org.springframework.stereotype.Service;

@Service
public class LookDirImpl implements LookDir {
    @Override
    public String getDir(String project) {

        return "searchPath:" + project;
    }
}
