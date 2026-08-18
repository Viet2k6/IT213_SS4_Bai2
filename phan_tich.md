# Phân Tích Các Phương Án Thiết Kế Lớp Dữ Liệu Bóc Tách

Bối cảnh: Hệ thống CRM cần bóc tách các tin nhắn thô gửi về từ tài xế thành thông tin có cấu trúc. 

Dưới đây là phân tích chi tiết về hai phương án dưới góc nhìn Lập trình phòng thủ (Defensive Programming), tính đóng gói, và các ràng buộc kỹ thuật của Hibernate/JPA.

## Phương án 1: Dùng trực tiếp lớp thực thể JPA Entity (IncidentReport) làm đối tượng đích

Sử dụng JPA Entity làm output cho `BeanOutputConverter` từ AI.

### Ưu điểm:
- **Phát triển nhanh chóng:** Code ngắn gọn, không cần tạo thêm các lớp DTO trung gian hay viết logic chuyển đổi (mapping).
- **Tiện lợi cho hệ thống nhỏ:** Phù hợp với các hệ thống prototype hoặc khi mô hình AI và database schema giống hệt nhau.

### Nhược điểm:
- **Vi phạm Lập trình phòng thủ (Defensive Programming):** AI có thể có những hiện tượng "ảo giác" (hallucination) hoặc trả về các field không hợp lệ. Nếu ánh xạ thẳng vào Entity, ta có nguy cơ bị ghi đè các trường quản trị hệ thống như `@Id`, `createdAt`, `status`, dẫn đến lỗ hổng Mass Assignment. Dữ liệu rác có thể được đưa trực tiếp vào database mà không qua màng lọc.
- **Tính đóng gói kém (Encapsulation):** Entity là đối tượng cốt lõi đại diện cho dữ liệu lưu trữ. Việc phơi bày Entity trực tiếp cho lớp giao tiếp bên ngoài (AI/LLM parser) làm mất đi sự đóng gói. Sự thay đổi trong cấu trúc Prompt hoặc output của AI sẽ buộc Entity phải thay đổi theo.
- **Ràng buộc của Hibernate/JPA:** JPA yêu cầu Entity phải có một constructor không tham số (No-args constructor) và thông thường sử dụng các Setter để gán dữ liệu. Điều này phá vỡ tính bất biến (Immutability), khiến đối tượng dễ bị thay đổi trạng thái một cách không mong muốn trong quá trình xử lý. Các trường `@Id` được `@GeneratedValue` tự động sinh ra cũng có thể bị AI vô tình gán giá trị làm gián đoạn quá trình persist.

---

## Phương án 2: Dùng một Java Record DTO (IncidentExtraction) làm đối tượng đích, sau đó map sang JPA Entity

Sử dụng một Record làm trung gian chứa dữ liệu từ AI, qua bước kiểm tra (validation) và chuyển đổi mới tạo Entity.

### Ưu điểm:
- **Lập trình phòng thủ xuất sắc:** Java Record tự nhiên là bất biến (Immutable). Dữ liệu sau khi được bóc tách từ LLM sẽ không thể bị thay đổi âm thầm. Ta có thể dễ dàng áp dụng các cơ chế Validation (như Hibernate Validator, hay check null/logic) trên DTO trước khi khởi tạo Entity. Điều này chặn đứng các dữ liệu rác hoặc độc hại xâm nhập vào hệ thống.
- **Tính đóng gói và Phân tách trách nhiệm (Separation of Concerns):** DTO chỉ làm nhiệm vụ đại diện cho kết quả của AI, Entity chỉ làm nhiệm vụ lưu trữ. Nếu tương lai AI đổi format trả về, ta chỉ cần đổi DTO và logic mapping, giữ nguyên Entity.
- **Tuân thủ tự nhiên các ràng buộc JPA:** Entity vẫn giữ được cấu trúc chuẩn của nó với `@Id @GeneratedValue`, các trường `@Version`, audit logs (`@CreatedDate`), và No-args constructor mà không phải thỏa hiệp để phục vụ JSON/LLM parser. Các trường `nullable` có thể được xử lý cẩn thận trong quá trình map từ DTO sang Entity.

### Nhược điểm:
- **Cần viết thêm code:** Phải định nghĩa thêm lớp DTO và logic để ánh xạ (map) từ DTO sang Entity.

---

## Kết luận

Dưới góc độ kỹ thuật phần mềm và Lập trình phòng thủ, **Phương án 2 (Sử dụng Java Record DTO làm trung gian)** là phương án tối ưu và duy nhất nên được chọn cho các hệ thống thực tế (như CRM). 

Dữ liệu từ LLM luôn chứa yếu tố không chắc chắn. Việc có một bước đệm (DTO) để validate và mapping dữ liệu giúp bảo vệ tính toàn vẹn của Database, đảm bảo an toàn cho ứng dụng và linh hoạt trong bảo trì.
