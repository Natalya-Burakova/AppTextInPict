# AppTextInPict
Приложение, которое будет производить генерирование изображения с передаваемым текстом в асинхронном режиме. 

- Количество потоков для обработки текста задается с помощью значения в поле аннотации. При старте приложения аннотация распознается и создается обработчик с указанным количеством потоков.

Запускается приложение и открывается стартовое окно. Варианты входных данных: {“text”:”something text”} - то есть пользователь указывает текст, который сгенерируется на картинке.

После нажатия на кнопку, открывается ссылка /api/picture/generate/{id} (где id - текст, который ввел пользователь):
- Если изображения нет и оно не обрабатывается возвращается ответ 200 с телом: {“link”:”ссылка на изображение, по которой оно будет доступно”}
- Если изображение уже существует с таким id - возвращается ошибка 400 с телом:{“message”:”изображение с таким id уже существует”,“link”:”ссылка на изображение”}
- Если изображение с таким id обрабатывается - возвращается 202 с телом:{“message”:”изображение с таким id обрабатывается”,“link”:”ссылка на изображение, по которой оно будет доступно”}

Пользователь может перейти по ссылке /api/picture/{id} (где id - текст, который ввел пользователь):
- Если изображение существует на диске, то пользователь наблюдает изображение на странице
- Если изображения не существует и не находится в обработке, возвращается 404 с телом:{“message”:”изображение не существует”}
- Если изображение с таким id обрабатывается - возвращается 202 с телом:{“message”:”изображение с таким id обрабатывается”}
 




