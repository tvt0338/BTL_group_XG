# Bài tập lớn OOP Java Bomberman Game

## Thành viên nhóm
### Tạ Văn Toàn
### Lê Công Minh
### Trương Văn Thành

## Mô tả
### Trong bài tập lớn này, nhiệm vụ của bạn là viết một phiên bản Java mô phỏng lại trò chơi Bomberman kinh điển của NES.


## Các đối tượng
### ![image](https://github.com/user-attachments/assets/91c9be0b-4aef-429b-9844-10a344ee1462) Player là nhân vật chính của trò chơi. Player có thể di chuyển theo 4 hướng trái/phải/lên/xuống theo sự điều khiển của người chơi.
### ![image](https://github.com/user-attachments/assets/0240dc15-ba1e-42f6-9a68-29b59574d9fe) Enemy là các đối tượng mà Bomber phải tiêu diệt hết để có thể qua Level. Enemy có thể di chuyển ngẫu nhiên hoặc tự đuổi theo Bomber tùy theo loại Enemy. Các loại Enemy sẽ được mô tả cụ thể ở phần dưới.
### ![image](https://github.com/user-attachments/assets/1d8118e6-3d76-4b4f-8b31-078e9ab36a45) Bomb là đối tượng được đặt theo vị trí người chơi và có tầm xa mặc định là 1 ô. Hướng nổ mặc định của bomb là hình dấu "+". Độ dài của bomb bị giới hạn lại nếu hướng nổ bị chặn bởi tường hoặc gạch và nổ theo mặc định nếu là người chơi và quái vật.

### Các loại enemy
### Balloom: Di chuyển ngẫu nhiên (Random), 1HP.
### Fly: Biết bay, đuổi theo Bomber, 1HP.
### Tanker : 3 hp, di chyển chậm .
### Void : tàn hình, di chuyển ngẫu nhiên, mỗi 3(s) xuất hiện một lần.
### Oneal: Tương tự Balloom, nhưng khi chạm mặt Bomberman, nó sẽ đuổi theo, 1HP.
