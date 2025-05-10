# Bài tập lớn OOP Java Bomberman Game

## Thành viên nhóm
### Tạ Văn Toàn
### Lê Công Minh
### Trương Văn Thành

## Mô tả
### Trong bài tập lớn này, nhiệm vụ của bạn là viết một phiên bản Java mô phỏng lại trò chơi Bomberman kinh điển của NES.


## Các đối tượng
### ![image](https://github.com/user-attachments/assets/161ab9c4-d9b5-47ac-b6bd-2fe98c66931b) Player là nhân vật chính của trò chơi. Player có thể di chuyển các hướng và đặt bomb theo điều khiển của người chơi thông qua các phím hỗ trợ , hp mặc định của player là 200 , nếu ăn item Health  ![image](https://github.com/user-attachments/assets/afd146a7-905e-4bee-a3a2-2fe7f8ada2c6) thì sẽ tăng 1 hp ; speed mặc định là 3 , nếu ăn item speed ![image](https://github.com/user-attachments/assets/349be316-d9bd-4b33-afb4-6af701092d34) thì speed sẽ tăng gấp đôi trong 1 khaongr thời gian . 

### - ![image](https://github.com/user-attachments/assets/07e6fd86-23d7-4033-9db3-148cca82ad62) Enemy là các đối tượng mà Bomber phải tiêu diệt hết để có thể qua map. Enemy có thể di chuyển ngẫu nhiên hoặc tự đuổi theo Bomber tùy theo loại Enemy. Các loại Enemy sẽ được mô tả cụ thể ở phần dưới.

### - ![image](https://github.com/user-attachments/assets/5e64c0ec-0b59-4b8f-b4b7-a017978251a0)Bomb là đối tượng để tiêu diệt các enemy , người chôi sử dụng phím space để đặt ra và sau vài giây sẽ phát nổ , hướng phát nổ của nó là hình dấu '+' , mặc định ban đầu phạm vi sẽ là 1 ô , nếu ăn item flame ![image](https://github.com/user-attachments/assets/06ff8cd5-01d0-4b82-a1f1-8c4cea4e3f1a) thì sẽ tăng thêm 1 đơn vị , phạm vi của bomb qua các map sẽ được reset lại . Nếu bomb nổ trúng bomber thì bomber sẽ -1hp , còn với các enemy thì sẽ chết luôn .

### - ![image](https://github.com/user-attachments/assets/d7de26f9-7d20-454a-b53c-3957ee173d9b) Brick  là đối tượng bị phá hủy bằng bomb nếu nằm trong phạm vi nổ  , và không thể băng cản phạm vi bomb nổ . Brick chặn được bomber và enemy di chuyển qua ( trừ fly). Không đặt bomb lên đucowj nếu đối tượng này chưa bị phá hủy .

### - ![image](https://github.com/user-attachments/assets/db0f32e8-0f37-419b-ab40-3738764d8c62) Grass là đối tượng mà Bomber và Enemy có thể di chuyển xuyên qua, và cho phép đặt Bomb lên vị trí của nó.

### - ![image](https://github.com/user-attachments/assets/e59af99c-0214-410c-91d9-4e53df9c1f84)  Wall là đối tượng cố định, không thể phá hủy bằng Bomb cũng như không thể đặt Bomb lên được, Bomber và Enemy không thể di chuyển vào đối tượng này.

### - ![image](https://github.com/user-attachments/assets/db0c2991-4a5c-4ac1-91ae-f40898387bf5) Portal là đối tượng xuất hiện sau khi clear hết enemy trong map , vị trí xuất hiện là ở bên phải phái cuối map , đi vào để tiếp tục sang các map khác .


# Items
### items sẽ xuất hiện cố định trên bản dồ , ăn chúng vào sẽ có hiệu quả như sau :


### ![image](https://github.com/user-attachments/assets/a084e370-8d78-4261-bc14-940f6170e7d9) God_eye là đối tượng giúp bomber nhìn thấy void khi chúng tàn hình .

### ![image](https://github.com/user-attachments/assets/158267f3-c6a3-47ba-9639-6dde179e605b) Flame tăng phạm vi nổ của bomb.

### ![image](https://github.com/user-attachments/assets/0732ac86-b8cb-4777-85b0-1c0cc35338ba) tăng tốc độ bomber lên gấp đôi .

### ![image](https://github.com/user-attachments/assets/1d7c2075-c2ad-40b1-9f4a-9c8730bb989a) tăng thêm 1hp cho bomber .



# Các loại enemy
### ![image](https://github.com/user-attachments/assets/f92de6a4-5253-4810-9dd1-a08ea57155d6) Balloom: Di chuyển ngẫu nhiên (Random).

### ![image](https://github.com/user-attachments/assets/d9862553-0ad7-425f-80f7-632db21a254c)  Fly: Biết bay , bay qua tường ,luôn đuổi theo Bomber.

### ![image](https://github.com/user-attachments/assets/440b0314-3091-4d2a-ad73-fad8cb8abd97) Tanker : di chuyển chậm hơn balloom , tìm đường đến bomber bằng thuật toán BFS .

### ![image](https://github.com/user-attachments/assets/39218fe9-6048-4267-9710-2c47790c847f) Void : có thể tàn hình,tìm đuòng đến bomber bằng thuật toán A*, tàn hình và hiện hình luân phiên theo tỉ lệ 7 : 3.

### ![image](https://github.com/user-attachments/assets/fecf7c5d-9b4c-4eca-a6fe-9e86a0dc2344) Oneal: Tương tự Balloom, nó sẽ đuổi theo bomber bằng thuật toán A* tương tự void .


# Mô tả cách thức xử lý 
- Bomber sẽ được di chuyển và đặt bomb bằng các nút hỗ trợ trong phần HELP có hướng dẫn , Bomber sẽ chỉ được di chuyển trên grass . bomber sẽ chết khi số hp = 0 .

- Enemy gây dame cho bomber bằng cách di chuyển và chạm vào bomber . Enemy sẽ chết nếu trong phạm vi nổ cuả bomb . Chúng cũng chỉ được phép di chuyển trên grass trừ enemy biết bay .

- Bomb vừa gây dame cho bomber vừa gây dame cho quái nếu ở trong phạm vi nổ . Bomb phá hủy được bricks . Phạm vi nổ sẽ tăng khi Bomber ăn item Flame . Phạm vi nổ của Bomb chỉ có Wall cản được .


# Mô tả cách bắt đầu Project 
- Chạy file MainMenu , sẽ có nút Play ấn vào đó sẽ tiến vào côt truyện , rồi tiếp tục tiến vào map1 để chơi rồi đến map2, map3 . Đi vào ![image](https://github.com/user-attachments/assets/7fef64d0-59e6-468d-8731-57dff9789291)
ở map3  là bạn sẽ chiến thắng .

