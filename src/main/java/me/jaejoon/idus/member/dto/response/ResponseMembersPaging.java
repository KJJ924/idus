package me.jaejoon.idus.member.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/07
 */

@Getter
@NoArgsConstructor
public class ResponseMembersPaging {

    private List<ResponseMemberList> elements;
    private int elementsSize;
    private int currentPage;
    private int totalPage;
    private int pageSize;

    private ResponseMembersPaging(Page<ResponseMemberList> listPage) {
        this.elements = listPage.getContent();
        this.elementsSize = elements.size();
        this.currentPage = listPage.getNumber();
        this.totalPage = listPage.getTotalPages();
        this.pageSize = listPage.getSize();
    }

    public static ResponseMembersPaging toMapper(Page<ResponseMemberList> listPage) {
        return new ResponseMembersPaging(listPage);
    }
}
