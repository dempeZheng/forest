package com.dempe.forest.example;

import com.dempe.forest.core.CompressType;
import com.dempe.forest.core.SerializeType;
import com.dempe.forest.core.annotation.Action;
import com.dempe.forest.core.annotation.Export;
import com.dempe.forest.core.annotation.Param;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/11/30
 * Time: 17:23
 * To change this template use File | Settings | File Templates.
 */
@Action("sample")
public interface SampleActionInterface {
    @Export(uri = "hello", compressType = CompressType.gizp, serializeType = SerializeType.kyro)
    String hello(@Param String word);
}
