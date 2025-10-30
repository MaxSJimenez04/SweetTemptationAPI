INSERT INTO Rol(rol) VALUES('Administrador');
INSERT INTO Rol(rol) VALUES('Empleado');
INSERT INTO Rol(rol) VALUES('Cliente');

INSERT INTO Usuario(usuario, contrasena, nombre, apellidos, correo, direccion, telefono, fechaRegistro, idRol) VALUES('STA_MaxSoto', 'Admin123', 'Maximiliano', 'Soto Jiménez', 'maximiliano.soto.j@gmail.com', 'Av. Independencia #22, Colonia Insurgentes, Ciudad de México', '+522284246249', GETDATE(), 1);
INSERT INTO Usuario(usuario, contrasena, nombre, apellidos, correo, direccion, telefono, fechaRegistro, idRol) VALUES('STE_LuisPerez', 'ContrasenaMuySegura', 'Luis', 'Pérez González','luis.perez@gmail.com', 'Calle Norte 2 #12, Colonia Centro, Puebla, Puebla', '+522225614821', GETDATE(), 2);
INSERT INTO Usuario(usuario, contrasena, nombre, apellidos, correo, direccion, telefono, fechaRegistro, idRol) VALUES('TeresaSuarez1989', 'qwerty1024', 'Teresa', 'Álvarez Suárez', 'ts1989@gmail.com','Calle Hollywood #12-B Colonia Europa, Ensenada, Baja California', '+526332154981', GETDATE(), 3);


INSERT INTO Categoria(nombre) VALUES('Pastel');
INSERT INTO Categoria(nombre) VALUES('Cupcake');
INSERT INTO Categoria(nombre) VALUES('Galleta');
INSERT INTO Categoria(nombre) VALUES('Tartas');
INSERT INTO Categoria(nombre) VALUES('Dona');
INSERT INTO Categoria(nombre) VALUES('Brownie');
INSERT INTO Categoria(nombre) VALUES('Mousse');
INSERT INTO Categoria(nombre) VALUES('Pay');
INSERT INTO Categoria(nombre) VALUES('Flan');
INSERT INTO Categoria(nombre) VALUES('Otro');

INSERT INTO Producto(nombre, descripcion,precio, disponible, unidades, fechaRegistro, categoria) VALUES('Pastel Chocolate 3Kg', 'Pastel de pan de chocolate, relleno de mermelada de fresa, cubierto de nutella, 1 piso', 125.00, 1, 12, GETDATE(), 1);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Pastel Tres Leches', 'Pastel clásico de tres leches con crema batida y fresas frescas', 122.00, 1, 10, GETDATE(), 1);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Pastel Red Velvet', 'Pan rojo aterciopelado con betún de queso crema', 270.00, 1, 8, GETDATE(), 1);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Pastel de Zanahoria', 'Pan húmedo con nuez y glaseado de queso crema', 240.50, 1, 6, GETDATE(), 1);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Pastel Vainilla Frutas', 'Pastel de vainilla con relleno de durazno y piña', 230.00, 1, 10, GETDATE(), 1);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Cupcake de Chocolate', 'Cupcake de chocolate con glaseado de cacao', 20.50, 1, 30, GETDATE(), 2);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Cupcake de Vainilla', 'Cupcake de vainilla con betún de mantequilla', 20.00, 1, 28, GETDATE(), 2);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Cupcake Red Velvet', 'Cupcake con betún de queso crema', 32.00, 1, 25, GETDATE(), 2);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Galleta Integral', 'Galleta de avena sin azúcar añadida', 10.00, 1, 22, GETDATE(), 3);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Galleta Rellena de Cajeta', 'Galleta de mantequilla con relleno de cajeta', 10.60, 1, 18, GETDATE(), 3);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Tarta de Frutas', 'Base de masa quebrada con crema pastelera y frutas frescas', 58.00, 1, 8, GETDATE(), 4);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Tarta de Limón', 'Tarta con crema de limón y merengue', 57.50, 1, 6, GETDATE(), 4);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Tarta de Queso', 'Cheesecake con base de galleta y mermelada de fresa', 59.00, 1, 10, GETDATE(), 4);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Tarta de Coco', 'Tarta con crema pastelera y coco rallado', 58.00, 1, 8, GETDATE(), 4);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Dona con Chispas', 'Dona con glaseado y chispas de colores', 31.40, 1, 25, GETDATE(), 5);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Dona Rellena de Cajeta', 'Dona rellena de cajeta con azúcar glas', 31.50, 1, 20, GETDATE(), 5);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Dona de Fresa', 'Dona con cobertura rosa sabor fresa', 35.00, 1, 28, GETDATE(), 5);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Dona con Nuez', 'Dona con glaseado de caramelo y trozos de nuez', 38.70, 1, 18, GETDATE(), 5);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Dona de Maple', 'Dona cubierta con glaseado sabor maple', 32.00, 1, 22, GETDATE(), 5);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Brownie Clásico', 'Brownie tradicional con textura húmeda y sabor intenso a cacao', 29.00, 1, 20, GETDATE(), 6);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Brownie con Nuez', 'Brownie con trozos de nuez tostada', 28.20, 1, 18, GETDATE(), 6);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Mousse de Chocolate', 'Postre cremoso de chocolate amargo', 160.00, 1, 8, GETDATE(), 7);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Mousse de Mango', 'Postre frío con puré natural de mango', 155.50, 1, 8, GETDATE(), 7);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Pay de Limón', 'Postre frío con galleta y crema de limón', 115.00, 1, 6, GETDATE(), 8);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Mini Pay de Queso', 'Pequeños pays de queso con mermelada', 94.00, 1, 9, GETDATE(), 8);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Flan Napolitano', 'Flan casero con caramelo líquido', 72.00, 1, 8, GETDATE(), 9);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Chocoflan', 'Combinación de pastel de chocolate y flan', 77.00, 1, 6, GETDATE(), 9);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Empanada de Cajeta', 'Empanada horneada con relleno de cajeta', 22.00, 1, 22, GETDATE(), 10);
INSERT INTO Producto(nombre, descripcion, precio, disponible, unidades, fechaRegistro, categoria) VALUES('Churros', 'Porción de 4 churros con azúcar y canela', 15.00, 1, 20, GETDATE(), 10);

INSERT INTO Pedido(fechaCompra, actual, total, estado, personalizado, idCliente) VALUES(GETDATE(), 1, 100.00, 1, 0, 3);
INSERT INTO Pedido (fechaCompra, actual, total, estado, personalizado, idCliente) VALUES (GETDATE(), 0, 156.00, 1, 0, 3);
INSERT INTO Pedido (fechaCompra, actual, total, estado, personalizado, idCliente) VALUES (GETDATE(), 0, 231.00, 1, 0, 3);
INSERT INTO Pedido (fechaCompra, actual, total, estado, personalizado, idCliente) VALUES (GETDATE(), 0, 199.10, 1, 0, 3);
INSERT INTO Pedido (fechaCompra, actual, total, estado, personalizado, idCliente) VALUES (GETDATE(), 0, 430.40, 1, 0, 3);
INSERT INTO Pedido (fechaCompra, actual, total, estado, personalizado, idCliente) VALUES (GETDATE(), 0, 308.60, 1, 0, 3);
INSERT INTO Pedido (fechaCompra, actual, total, estado, personalizado, idCliente) VALUES (GETDATE(), 0, 125.00, 1, 1, 3);

INSERT INTO ProductoPedido(cantidad, subtotal, idPedido, idProducto) VALUES(5, 100, 1 ,7);
INSERT INTO ProductoPedido (cantidad, subtotal, idPedido, idProducto) VALUES (3, 61.50, 2, 6);
INSERT INTO ProductoPedido (cantidad, subtotal, idPedido, idProducto) VALUES (4, 40.00, 2, 7);
INSERT INTO ProductoPedido (cantidad, subtotal, idPedido, idProducto) VALUES (5, 51.10, 2, 9);
INSERT INTO ProductoPedido (cantidad, subtotal, idPedido, idProducto) VALUES (2, 116.00, 3, 11);
INSERT INTO ProductoPedido (cantidad, subtotal, idPedido, idProducto) VALUES (2, 115.00, 3, 12);
INSERT INTO ProductoPedido (cantidad, subtotal, idPedido, idProducto) VALUES (2, 63.20, 4, 15);
INSERT INTO ProductoPedido (cantidad, subtotal, idPedido, idProducto) VALUES (2, 70.60, 4, 17);
INSERT INTO ProductoPedido (cantidad, subtotal, idPedido, idProducto) VALUES (1, 38.70, 4, 18);
INSERT INTO ProductoPedido (cantidad, subtotal, idPedido, idProducto) VALUES (1, 26.60, 4, 19);
INSERT INTO ProductoPedido (cantidad, subtotal, idPedido, idProducto) VALUES (4, 116.80, 5, 20);
INSERT INTO ProductoPedido (cantidad, subtotal, idPedido, idProducto) VALUES (2, 56.40, 5, 21);
INSERT INTO ProductoPedido (cantidad, subtotal, idPedido, idProducto) VALUES (1, 160.00, 5, 22);
INSERT INTO ProductoPedido (cantidad, subtotal, idPedido, idProducto) VALUES (1, 97.20, 5, 23);
INSERT INTO ProductoPedido (cantidad, subtotal, idPedido, idProducto) VALUES (1, 115.00, 6, 24);
INSERT INTO ProductoPedido (cantidad, subtotal, idPedido, idProducto) VALUES (1, 94.00, 6, 25);
INSERT INTO ProductoPedido (cantidad, subtotal, idPedido, idProducto) VALUES (1, 72.00, 6, 26);
INSERT INTO ProductoPedido (cantidad, subtotal, idPedido, idProducto) VALUES (1, 27.60, 6, 27);

INSERT INTO PedidoPersonalizado(idPedido, cobertura, especificaciones, relleno, saborBizcocho, tamano, fechaCompra, fechaSolicitud) VALUES(7, 'Chocolate', 'Frase "Feliz cumpleaños"', 'Durazno', 'Vainilla', '2 pisos', 2025-11-15, GETDATE());


INSERT INTO Pago(idPedido, tipoPago, total) VALUES(2, 'Efectivo', 156.00);
INSERT INTO Pago(idPedido, tipoPago,cuenta, total) VALUES(3, 'Tarjeta','1521125223011452' ,231.00);
INSERT INTO Pago(idPedido, tipoPago,cuenta, total) VALUES(4, 'Tarjeta','8121910220511422' ,199.10);
INSERT INTO Pago(idPedido, tipoPago,cuenta, total) VALUES(5, 'Tarjeta','0125852023012302' ,430.40);
INSERT INTO Pago(idPedido, tipoPago,cuenta, total) VALUES(6, 'Tarjeta','9205205021227108' ,308.60);
INSERT INTO Pago(idPedido, tipoPago,cuenta, total) VALUES(7, 'Tarjeta','5008185225585060' ,125.00);