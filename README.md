# Bài tập lớn OOP Java Bomberman Game

## Thành viên nhóm
### Tạ Văn Toàn
### Lê Công Minh
### Trương Văn Thành

## Mô tả
### Trong bài tập lớn này, nhiệm vụ của bạn là viết một phiên bản Java mô phỏng lại trò chơi Bomberman kinh điển của NES.


## Các đối tượng
### - ![down_1.png](src/images/player/down_1.png)Player là nhân vật chính của trò chơi. Player có thể di chuyển các hướng và đặt bomb theo điều khiển của người chơi thông qua các phím hỗ trợ , hp mặc định của player là 200 , nếu ăn item Health  ![tym.gif](src/items/images/tym.gif) thì sẽ tăng 1 hp ; speed mặc định là 3 , nếu ăn item speed ![speed.gif](src/items/images/speed.gif) thì speed sẽ tăng gấp đôi trong 1 khaongr thời gian . 

### - ![fly.gif](src/enemy/images/fly.gif) Enemy là các đối tượng mà Bomber phải tiêu diệt hết để có thể qua map. Enemy có thể di chuyển ngẫu nhiên hoặc tự đuổi theo Bomber tùy theo loại Enemy. Các loại Enemy sẽ được mô tả cụ thể ở phần dưới.

### - ![bomb.png](src/bomb/bomb.png) Bomb là đối tượng để tiêu diệt các enemy , người chôi sử dụng phím space để đặt ra và sau vài giây sẽ phát nổ , hướng phát nổ của nó là hình dấu '+' , mặc định ban đầu phạm vi sẽ là 1 ô , nếu ăn item flame ![flame.gif](src/items/images/flame.gif) thì sẽ tăng thêm 1 đơn vị , phạm vi của bomb qua các map sẽ được reset lại . Nếu bomb nổ trúng bomber thì bomber sẽ -1hp , còn với các enemy thì sẽ chết luôn .

### - ![destructiblewall.png](src/images/map/destructiblewall.png) Brick  là đối tượng bị phá hủy bằng bomb nếu nằm trong phạm vi nổ  , và không thể băng cản phạm vi bomb nổ . Brick chặn được bomber và enemy di chuyển qua ( trừ fly). Không đặt bomb lên đucowj nếu đối tượng này chưa bị phá hủy .

### - ![grass.png](src/images/map/grass.png) Grass là đối tượng mà Bomber và Enemy có thể di chuyển xuyên qua, và cho phép đặt Bomb lên vị trí của nó.

### - ![wall.png](src/images/map/wall.png) Wall Wall là đối tượng cố định, không thể phá hủy bằng Bomb cũng như không thể đặt Bomb lên được, Bomber và Enemy không thể di chuyển vào đối tượng này.

### - ![portal.gif](src/portal/portal.gif) Portal là đối tượng xuất hiện sau khi clear hết enemy trong map , vị trí xuất hiện là ở bên phải phái cuối map , đi vào để tiếp tục sang các map khác .


# Items
### items sẽ xuất hiện cố định trên bản dồ , ăn chúng vào sẽ có hiệu quả như sau :


-![eye.gif](src/items/images/eye.gif) God_eye là đối tượng giúp bomber nhìn thấy void khi chúng tàn hình .

-![flame.gif](src/items/images/flame.gif) Flame tăng phạm vi nổ của bomb.

-![speed.gif](src/items/images/speed.gif) tăng tốc độ bomber lên gấp đôi .

-![tym.gif](src/items/images/tym.gif) tăng thêm 1hp cho bomber .



# Các loại enemy
### ![balloom_down.png](src/enemy/images/balloom_down.png)Balloom: Di chuyển ngẫu nhiên (Random).

### ![fly.gif](src/enemy/images/fly.gif)Fly: Biết bay , bay qua tường ,luôn đuổi theo Bomber.

### ![tanker_left1.png](src/enemy/images/tanker_left1.png)Tanker : di chuyển chậm hơn balloom , tìm đường đến bomber bằng thuật toán BFS .

### ![void_left1.png](src/enemy/images/void_left1.png)Void : có thể tàn hình,tìm đuòng đến bomber bằng thuật toán A*, tàn hình và hiện hình luân phiên theo tỉ lệ 7 : 3.

### ![oneal_left1.png](src/enemy/images/oneal_left1.png)Oneal: Tương tự Balloom, nó sẽ đuổi theo bomber bằng thuật toán A* tương tự void .


# Mô tả cách thức xử lý 
- Bomber sẽ được di chuyển và đặt bomb bằng các nút hỗ trợ trong phần HELP có hướng dẫn , Bomber sẽ chỉ được di chuyển trên grass . bomber sẽ chết khi số hp = 0 .

- Enemy gây dame cho bomber bằng cách di chuyển và chạm vào bomber . Enemy sẽ chết nếu trong phạm vi nổ cuả bomb . Chúng cũng chỉ được phép di chuyển trên grass trừ enemy biết bay .

- Bomb vừa gây dame cho bomber vừa gây dame cho quái nếu ở trong phạm vi nổ . Bomb phá hủy được bricks . Phạm vi nổ sẽ tăng khi Bomber ăn item Flame . Phạm vi nổ của Bomb chỉ có Wall cản được .


# Mô tả cách bắt đầu Project 
- Chạy file MainMenu , sẽ có nút Play ấn vào đó sẽ tiến vào côt truyện , rồi tiếp tục tiến vào map1 để chơi rồi đến map2, map3 . Đi vào portal![portal.webp](src/portal/portal.webp) ở map3  là bạn sẽ chiến thắng .

