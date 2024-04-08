<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" import="com.hyeongseok.websocket.dto.ChatRoomList" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chat Room List</title>
</head>
<body>
<div class="row">
    <h3>채팅방 리스트</h3>
</div>
<div class="row">
    <ul>
        <% for (ChatRoomList chatRoom : (List<ChatRoomList>) request.getAttribute("chatRooms")) { %>
        <%-- 각 리스트 아이템을 클릭 가능한 링크로 변경합니다. --%>
        <li><a href="/chatroom/<%= chatRoom.getChatroom_id() %>"><%= chatRoom.getChatroom_id() + " 번 채팅방"%></a></li>
        <% } %>
    </ul>
</div>
</body>
</html>
