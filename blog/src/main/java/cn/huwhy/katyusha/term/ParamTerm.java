package cn.huwhy.katyusha.term;

import com.jfinal.plugin.mybatis.Term;

public class ParamTerm extends Term {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
