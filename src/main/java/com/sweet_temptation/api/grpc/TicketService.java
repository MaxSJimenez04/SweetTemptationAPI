package com.sweet_temptation.api.grpc;
import com.google.protobuf.ByteString;
import com.sweet_temptation.api.dto.DetallesProductoDTO;
import com.sweet_temptation.api.dto.PedidoDTO;
import com.sweet_temptation.api.model.Pedido;
import com.sweet_temptation.api.repository.PagoRepository;
import com.sweet_temptation.api.repository.PedidoRepository;
import com.sweet_temptation.api.repository.ProductoRepository;
import com.sweettemptation.grpc.TicketRequest;
import com.sweettemptation.grpc.TicketResponse;
import com.sweettemptation.grpc.TicketServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@GrpcService
public class TicketService extends TicketServiceGrpc.TicketServiceImplBase{

    private ProductoRepository productoRepository;
    private PedidoRepository pedidoRepository;
    private PDType1Font fuente = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    private PDType1Font fuenteNegrita = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

    public TicketService(ProductoRepository productoRepository, PedidoRepository pedidoRepository) {
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public void generarTicket(TicketRequest request, StreamObserver<TicketResponse> responseObserver) {
        if (request == null || request.getIdPedido() <= 0) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("El idPedido es requerido y debe ser mayor a 0")
                            .asRuntimeException()
            );
            return;
        }
        List<DetallesProductoDTO> productos = obtenerProductos(request.getIdPedido());
        PedidoDTO detallesPedido = obtenerDetallesPedido(request.getIdPedido());
        byte[] ticket = crearEstructura(productos, detallesPedido);
        String filename = "ticket_pedido" + detallesPedido.getId();
        TicketResponse respuesta = TicketResponse.newBuilder().setFileName(filename).setPdf(ByteString.copyFrom(ticket)).build();
        responseObserver.onNext(respuesta);
        responseObserver.onCompleted();
    }


    private List<DetallesProductoDTO> obtenerProductos(int idPedido){
        List<DetallesProductoDTO> lista = productoRepository.obtenerListaProductos(idPedido);
        return lista;
    }

    private PedidoDTO obtenerDetallesPedido(int idPedido){
        Pedido respuesta = pedidoRepository.findById(idPedido).orElse(null);
        if (respuesta != null) {
            return new PedidoDTO(respuesta.getId(), respuesta.getFechaCompra(), respuesta.getActual(),respuesta.getTotal()
            , respuesta.getEstado(), respuesta.getPersonalizado(), respuesta.getIdCliente());
        }else{
            return null;
        }
    }

    private byte[] crearEstructura(List<DetallesProductoDTO> lista, PedidoDTO detallesPedido){
        byte[] ticketArray;
        try(ByteArrayOutputStream out = new ByteArrayOutputStream();
            PDDocument ticket = new PDDocument()){
            float anchoTicket = 230f;
            float margenIzq = 15f;
            float margenDer = 15f;
            float margenArriba = 10f;
            float margenAbajo = 10f;
            int tamanoTitulos = 25;
            int tamanoFuente = 20;
            int tamanoFuenteProductos = 14;
            int tamanoTotal = 17;
            String fechaCompra = detallesPedido.getFechaCompra().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            PDPage pagina = new PDPage(new PDRectangle(anchoTicket, PDRectangle.LETTER.getHeight()));
            ticket.addPage(pagina);

            PDPageContentStream contentStream = new PDPageContentStream(ticket, pagina);

            float y = pagina.getMediaBox().getHeight() - margenArriba;

            PDImageXObject imagen = PDImageXObject.createFromFile("src/main/resources/iconos/img.png", ticket);
            float anchoImagen = 100;
            float alturaImagen = 100;
            float x = (anchoImagen - anchoImagen) / 2;
            contentStream.drawImage(imagen, x, y, anchoImagen, alturaImagen);
            y -= 10;
            contentStream.setFont(fuente, 20);
            centrarTexto(contentStream, fuenteNegrita, tamanoTitulos, "SWEET TEMPTATION", anchoTicket, y);
            y -= 5;
            centrarTexto(contentStream, fuenteNegrita, tamanoFuente, "Repostería", anchoTicket, y);
            y -= 5;
            centrarTexto(contentStream, fuente, tamanoFuente, "Recibo de compra", anchoTicket, y);
            y -= 5;
            centrarTexto(contentStream, fuente, tamanoFuente, "No. Pedido: " + detallesPedido.getId(), anchoTicket, y);
            y -= 5;
            centrarTexto(contentStream, fuente, tamanoFuente,
                    "Fecha de compra: " + fechaCompra,
                    anchoTicket, y);
            y -= 10;
            float anchoSeparacion = anchoTicket - margenIzq - margenDer;
            int totalGato = (int)(anchoSeparacion / (fuente.getStringWidth(".") / 1000 * tamanoFuente));
            String gato = "#".repeat(Math.max(0, totalGato));
            contentStream.beginText();
            contentStream.newLineAtOffset(margenIzq, y);
            contentStream.showText(gato);
            contentStream.endText();
            for(DetallesProductoDTO detalles: lista){
                if (y <= margenArriba + 15) {

                    contentStream.close(); // ← AQUÍ ES CORRECTO

                    pagina = new PDPage(PDRectangle.LETTER);
                    ticket.addPage(pagina);

                    // Nueva página → nuevo stream
                    contentStream = new PDPageContentStream(ticket, pagina);

                    y = pagina.getMediaBox().getHeight() - margenArriba;
                }
                String cantidad = Integer.toString(detalles.getCantidad());
                String precio = detalles.getPrecio().toString();
                escribirProductos(contentStream, fuente, tamanoFuenteProductos, anchoTicket, y, margenIzq, margenDer,
                        cantidad, detalles.getNombre(), precio);
                y -= 7;
            }
            contentStream.beginText();
            contentStream.newLineAtOffset(margenIzq, y);
            contentStream.showText(gato);
            contentStream.endText();
            y -= 10;
            BigDecimal totalSinIva = calcularSinIva(detallesPedido.getTotal());
            escribirTotal(contentStream, fuente, tamanoTotal, margenIzq, margenDer, y, anchoSeparacion,
                    "Subtotal", totalSinIva.toString());
            y -= 5;
            escribirTotal(contentStream, fuente, tamanoTotal, margenIzq, margenDer, y, anchoSeparacion,
                    "IVA: ", "16%");
            y -= 5;

            escribirTotal(contentStream, fuenteNegrita, tamanoTotal, margenIzq, margenDer, y,
                    anchoSeparacion,"Total: " ,detallesPedido.getTotal().toString());
            y -= 5;
            contentStream.beginText();
            contentStream.newLineAtOffset(margenIzq, y);
            contentStream.showText(gato);
            contentStream.endText();
            y -= 5;
            centrarTexto(contentStream, fuente, tamanoTitulos, "GRACIAS POR TU COMPRA", anchoTicket, y);

            contentStream.close();
            ticket.save(out);
            ticketArray = out.toByteArray();
        }catch(Exception e){
            e.printStackTrace();
            ticketArray = null;
        }
        return ticketArray;
    }


    public void centrarTexto(PDPageContentStream cs, PDFont fuente, float tamano,
                                 String texto, float anchoPagina, float y) throws IOException {

        float textWidth = fuente.getStringWidth(texto) / 1000 * tamano;
        float startX = (anchoPagina - textWidth) / 2;

        cs.beginText();
        cs.setFont(fuente, tamano);
        cs.newLineAtOffset(startX, y);
        cs.showText(texto);
        cs.endText();
    }

    public void escribirProductos(PDPageContentStream cs, PDFont fuente, float tamanoFuente,
                               float anchoPagina, float y,float margenIzq, float margenDer,
                               String cantidad, String nombre, String precio) throws IOException {

        float startX = margenIzq;
        float colCantWidth = 20f;
        float colPrecioWidth = 45f;
        // Calcular espacios restantes para el nombre y el relleno
        float anchoUtil = anchoPagina - margenIzq - margenDer;
        float espacioNombreYPuntos = anchoUtil - colCantWidth - colPrecioWidth;
        float nombreWidth = fuente.getStringWidth(nombre) / 1000 * tamanoFuente;
        // Ajustar nombre si es demasiado largo
        String nombreFinal = nombre;
        if (nombreWidth > espacioNombreYPuntos) {

            // Reducir texto hasta que quepa
            while (fuente.getStringWidth(nombreFinal + "...") / 1000 * tamanoFuente > espacioNombreYPuntos) {
                if (nombreFinal.length() <= 1) break;
                nombreFinal = nombreFinal.substring(0, nombreFinal.length() - 1);
            }

            nombreFinal += "...";
        }

        cs.beginText();
        cs.setFont(fuente, tamanoFuente);
        cs.newLineAtOffset(startX, y);
        cs.showText(cantidad);
        cs.endText();

        float nombreX = startX + colCantWidth;
        cs.beginText();
        cs.newLineAtOffset(nombreX, y);
        cs.showText(nombreFinal);
        cs.endText();

        // Relleno puntos "...."
        float nombreFinalWidth = fuente.getStringWidth(nombreFinal) / 1000 * tamanoFuente;
        float dotsStartX = nombreX + nombreFinalWidth;

        float anchoPuntos = espacioNombreYPuntos - nombreFinalWidth;
        int dotsCount = (int)(anchoPuntos / (fuente.getStringWidth(".") / 1000 * tamanoFuente));
        String dots = ".".repeat(Math.max(0, dotsCount));

        cs.beginText();
        cs.newLineAtOffset(dotsStartX, y);
        cs.showText(dots);
        cs.endText();

        float priceX = margenIzq + anchoUtil - colPrecioWidth;
        cs.beginText();
        cs.newLineAtOffset(priceX, y);
        cs.showText(precio);
        cs.endText();
    }

    private void escribirTotal(PDPageContentStream content, PDType1Font fuente, float tamanoFuente,
                               float margenIzq, float margenDer, float y, float anchoMaximo, String total,
                                   String valor) throws IOException {

        float anchoUtil = anchoMaximo - margenIzq - margenDer;
        content.setFont(fuente, tamanoFuente);
        float valorWidth = fuente.getStringWidth(valor) / 1000 * tamanoFuente;
        float rightX = margenIzq + (anchoUtil - valorWidth);

        content.beginText();
        content.newLineAtOffset(margenIzq, y);
        content.showText(total);
        content.endText();

        content.beginText();
        content.newLineAtOffset(rightX, y);
        content.showText(valor);
        content.endText();
    }

    BigDecimal calcularSinIva(BigDecimal valor) {
        double valorParseado = Double.parseDouble(valor.toString());
        double valorSinIva = valorParseado / 1.16;
        return BigDecimal.valueOf(valorSinIva);
    }

}
