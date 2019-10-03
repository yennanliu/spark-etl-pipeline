<h1 align="center">SPARK-ETL-PIPELINE</h1>
<h4 align="center">demo various data fetch/transform process via Spark Scala </h4>

## Quick Start
```bash
# STEP 0) 
$ git clone https://github.com/yennanliu/spark-scala-word-count.git && cd spark-scala-word-count 

# STEP 1) download the used dependencies.
$ sbt clean compile

# STEP 2) print twitter via spark stream  via sbt run`
$ sbt run

# Multiple main classes detected, select one to run:

#  [1] StreamHelloworld.PrintTwitterStream
#  [2] mydevclass.load_creds_dev
#  [3] sparkhelloworld.SparkHelloWorld
#  [4] sparkrddrelation.SparkRDDRelation
#  [5] sparksqldemo.SparkSQLDemo

# Enter number: 1

# [info] Running StreamHelloworld.PrintTwitterStream 
# 19/10/03 16:13:34 WARN NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
# >>>>> start stream...
# -------------------------------------------
# Time: 1570090419000 ms
# -------------------------------------------
# .
# .
# .

# -------------------------------------------
# Time: 1570090423000 ms
# -------------------------------------------
# @bq6XJCWficRD06p 헉
# RT @ippatel: रायबरेली का किला भी ढहने की ओर है। अदिति सिंह समेत बसपा के अनिल सिंह व सपा के नितिन अग्रवाल भी शीघ्र ही पाला बदल सकते हैं।
# 손 갈아끼우는게 빠른듯
# RT @sho_ngki: これ全部パチンコ屋に並んでるって
# やらない人からしたら考えられないだろうね…みんなが煙たがってるパチスロはタピオカよりあちーのよ。笑 ただピンク頭の誕生日ってだけなんやで。笑 座れた方々、魔女狩り頑張ってください🤗笑 https://t.co/Zaz…
# RT @konotarogomame: 米第７艦隊司令官による表敬。
# チャレンジコインを交換しました。 https://t.co/1NNotrB8RV
# RT @PAYBACK_Careers: In der letzten Woche haben wir Johannes bei seiner Bachelorprüfung besucht. Heute verabschieden wir ihn nach Vietnam -…
# RT @YorozuyGintamam: クソ客「お客様は神様だろ‼︎‼︎‼︎‼︎」 https://t.co/gojfw231y2
# RT @c3rmenDraws: It's spooky month so time to remind everyone that adopting a hellhound is not just for life but for afterlife as well! I m…
# RT @_conradsleet: biggest plot twist of my generation https://t.co/btYNWfb9qA
# RT @TasbehEstigfar: اللهم إني استغفرك عدد ما أنساني الشيطان ذكرك وعدد ماألهتني الدنيا عن الـرجوع إليك فقد قصرت ولم تقصر ونسيتك ولم تنساني ا…
# ...

# -------------------------------------------
# Time: 1570090424000 ms
# -------------------------------------------
# @kagome_oinu あらま....

# 尊い....
# RT @aulmaulidiana: Gue aja gatau napa nih muka kalo lewat depan orang ga dikenal keliatannya judes, sombong.
# Padahal kalo udah kenal mah Ya…
# @karisumanhikaru ください
# @YahooNewsTopics 国民が馬鹿だからこんなことになるんだ。
# テレビを見て、スマホ見て、ゲームして、政治なんてめんどくさい。なんてしてるからこんなことになるんだ。こんなの見ても何にも感じない奴らばかりだからこんなことになるんだ。
# RT @VijayFansTrends: #BIGIL Teaser Announcement SOON..😎 

# #Thalapathy64Pooja
# @flare_KH ふれあん！！！！！横浜公園！チケット2枚あるんだけどどう！？！？！？
# RT @HeyBudie: Pelan-pelan diabaikan,
# Pelan-pelan tak ada kabar,
# Pelan-pelan ditinggalkan.
# @____OSH__94 편의점 가야하나
# RT @modelpress: ジャニーズWEST“アウェー戦”に悪戦苦闘 重岡大毅「番組史上No.1のドタバタ」＜ #パパジャニWEST ＞ #ジャニーズWEST #重岡大毅 #桐山照史 #中間淳太 #神山智洋 #藤井流星 #濵田崇裕 #小瀧望

# https://t.co/e…
# @Lino_BD ずっと昔に送ったからじゃないかな
# たぶんりのさんから送ってもらえればいける
# ...

# -------------------------------------------
# Time: 1570090425000 ms
# -------------------------------------------
# 手塚治虫の火の鳥が好きすぎるんやけどsasakureさんの曲ってどこか火の鳥っぽさあって(まんま火の鳥楽曲もあるけど)曲聴きながら火の鳥読むの最高なんだよ〜〜
# RT @cnn_co_jp: 木星に浮かぶ巨大な「黒い円」発見、直径３５４０キロ　ＮＡＳＡ https://t.co/9Auj2D1NaF
# RT @Angryblue911: "달쇼통 하야 & 쪼구기 사퇴" 집회 생방 중에 있습니다.

# -오른소리
# -신의 한수
# -펜앤드마이크

# 정말 엄청 모였는데 열기가 뜨겁습니다.
# RT @iyashichannel_: 犬の被り物をしたら大人気！https://t.co/oa2XnQ1Gti
# RT @Muthia911: “Dear future child,


# # STEP 3) create jars from spark scala scriots 
# $ sbt assembly


```

## Quick Start (Docker)
```bash 
# STEP 0) 
$ git clone https://github.com/yennanliu/spark-scala-word-count.git

# STEP 1) 
$ cd spark-scala-word-count

# STEP 2) docker build 
$ docker build . -t spark_env

# STEP 3) ONE COMMAND : run the docker env and sbt compile and sbt run and assembly once 
$ docker run  --mount \
type=bind,\
source="$(pwd)"/.,\
target=/spark-word-count \
-i -t spark_env \
/bin/bash  -c "cd ../spark-word-count && sbt clean compile && sbt run && sbt assembly"

# STEP 3') : STEP BY STEP : access docker -> sbt clean compile -> sbt run -> sbt assembly -> spark-submit 
# docker run 
$ docker run  --mount \
type=bind,\
source="$(pwd)"/.,\
target=/spark-word-count \
-i -t spark_env \
/bin/bash 
# inside docker bash 
root@942744030b57:~ cd ../spark-word-count && sbt clean compile && sbt run 

```

## Ref 

<details>
<summary>Ref</summary>

- Stream via python socket 
	- https://pythonprogramming.net/buffering-streaming-data-sockets-tutorial-python-3/
- Install spark + yarn + hadoop via docker 
	- https://medium.com/@thiagolcmelo/submitting-a-python-job-to-apache-spark-on-docker-b2bd19593a06
	- https://www.svds.com/develop-spark-apps-on-yarn-using-docker/

</details>

## Dataset 

<details>
<summary>Dataset</summary>

- Twitch API (`stream`)
	- https://dev.twitch.tv/docs/v5/reference/streams/
- Dota2 API (`stream`)
	- https://docs.opendota.com/#section/Authentication
- NYC TLC Trip Record dataset (taxi) (`large dataset`)
	- https://www1.nyc.gov/site/tlc/about/tlc-trip-record-data.page
- Amazon Customer Reviews Dataset  (`large dataset`)
	- https://registry.opendata.aws/amazon-reviews/
- Github repo dataset (`large dataset`)
	- https://www.kaggle.com/github/github-repos
- Hacker news dataset (`large dataset`)
 	- https://www.kaggle.com/hacker-news/hacker-news
- Stackoverflow dataset (`large dataset`)
	- https://www.kaggle.com/stackoverflow/stackoverflow
- Yelp dataset (`large dataset`)
	- https://www.kaggle.com/yelp-dataset/yelp-dataset
- Relational dataset (RDBMS online free dataset)
	- https://relational.fit.cvut.cz/search

</details> 