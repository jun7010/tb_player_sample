const express = require("express");
const app = express();
const path = require("path");
const publicPath = path.join(__dirname, "public");
const multer = require("multer");

app.use(express.static(publicPath));

const upload = multer({
  storage: multer.diskStorage({
    filename(req, file, done) {
      done(null, "sample.png");
    },
    destination(req, file, done) {
      done(null, path.join(__dirname, "public"));
    },
  }),
  limits: { fileSize: 10 * 1024 * 1024 },
});

const uploadMiddleware = (req, res, next) => {
    upload.single("myFile")(req, res, (err) => {
      if (err) {
        console.error(err); // 에러 출력
        return res.status(500).json({ error: 'File upload failed.' });
      }
      next();
    });
  };

app.post("/upload", uploadMiddleware, (req, res) => {
  console.log(req.file);

  const data_list = [
    {
      img_name:
        'https://newsroom-prd-data.s3.ap-northeast-2.amazonaws.com/wp-content/uploads/2022/11/SKT%EB%89%B4%EC%8A%A4%EB%A3%B8_T%EB%B8%8C%EB%9E%9C%EB%93%9C%EB%A6%AC%EB%89%B4%EC%96%BC1_1000_PC.png',
      link_url: 'https://google.com',
      text1: '샘플1',
      text2: '샘플2',
    },
    {
        img_name:
          'https://newsroom-prd-data.s3.ap-northeast-2.amazonaws.com/wp-content/uploads/2022/11/SKT%EB%89%B4%EC%8A%A4%EB%A3%B8_T%EB%B8%8C%EB%9E%9C%EB%93%9C%EB%A6%AC%EB%89%B4%EC%96%BC1_1000_PC.png',
        link_url: 'https://google.com',
        text1: '샘플3',
        text2: '샘플4',
    },
    {
        img_name:
          'https://newsroom-prd-data.s3.ap-northeast-2.amazonaws.com/wp-content/uploads/2022/11/SKT%EB%89%B4%EC%8A%A4%EB%A3%B8_T%EB%B8%8C%EB%9E%9C%EB%93%9C%EB%A6%AC%EB%89%B4%EC%96%BC1_1000_PC.png',
        link_url: 'https://google.com',
        text1: '샘플5',
        text2: '샘플6',
    }
  ];


  console.log("data return : ", data_list);
  res.json(data_list);
});

app.listen(5001, () => {
  console.log("server is running at 5001");
});