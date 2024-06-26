package com.ank.pedidos.controllers.mapper;

import com.ank.pedidos.controllers.dto.PedidoResponse;
import com.ank.pedidos.entities.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper
public interface PedidoMapper {
    PedidoMapper INSTANCE = Mappers.getMapper(PedidoMapper.class);

    @Mapping(target="clienteNome", source="cliente.nome")
    @Mapping(target="clienteEndereco", source="cliente.endereco")
    @Mapping(target="clienteCpfCnpj", source="cliente.cpfcnpj")
    @Mapping(target="clienteTelefone", source="cliente.telefone")
    PedidoResponse toResponse(Pedido pedido);

    List<PedidoResponse> toResponse(List<Pedido> pedidos);


    default Page<PedidoResponse> toResponse(Page<Pedido> pedidos) {
        List<PedidoResponse> pedidoResponses = toResponse(pedidos.getContent());
        return new PageImpl<>(pedidoResponses, pedidos.getPageable(), pedidos.getTotalElements());
    }

}
