package me.jaejoon.idus.order.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.jaejoon.idus.member.exception.NotFoundMemberException;
import me.jaejoon.idus.member.repository.MemberRepository;
import me.jaejoon.idus.order.dto.response.ResponseOrder;
import me.jaejoon.idus.order.dto.response.ResponseOrderList;
import me.jaejoon.idus.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

/**
 * @author dkansk924@naver.com
 * @since 2021/07/07
 */

@Service
@RequiredArgsConstructor
public class OrderAdminService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    public ResponseOrderList getMemberOrderList(String userEmail) {
        if (!memberRepository.existsByEmail(userEmail)) {
            throw new NotFoundMemberException();
        }

        List<ResponseOrder> orders = orderRepository.findByConsumer(userEmail)
            .stream()
            .map(ResponseOrder::toMapper)
            .collect(Collectors.toList());

        return new ResponseOrderList(userEmail, orders);
    }


}
