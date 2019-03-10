package escape_game;
/*
 * タイトル:気が付いたら部屋にいました
 * 設定: 1階建ての家...玄関から出たらクリア
 * 　　　　 :　頭使う系
 * 素材：キッチン、玄関、脱衣所など
 */

// 実行/実行構成/パラメータ(タブ)で幅,高さのパラメータ変更を推奨(500,500)

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("serial")
public class Mainpro extends Applet implements KeyListener, MouseListener, MouseMotionListener, Runnable {
	String screenMode = "Title";
	/*
	 * Title   :タイトル
	 * Tuto:チュートリアル
	 * WesternStyleRoom:洋室
	 * Dk:ダイニングキッチン(kが小文字であることに注意)
	 * Entrance:玄関
	 * japaneseStyleRoom1:和室1
	 * japaneseStyleRoom2:和室2
	 * datuijo:脱衣所
	 * WC:トイレ
	 */
	ArrayList<String> bag = new ArrayList<String>();
	int screen_size_x = 500;
	int screen_size_y = 500;
	//画面のインスタンス
	NO_FIELD nothing_field = new NO_FIELD();
	Title title = new Title();;
	Tutorial tuto = new Tutorial();
	WesternStyleRoom westernStyleRoom;
	Dk dk;
	DkTop dkTop;
	Entrance entrance;
	JapaneseStyleRoom1 japaneseStyleRoom1;
	JapaneseStyleRoom2 japaneseStyleRoom2;
	Datuijo datuijo;
	Bathroom bathroom;
	WC wc;
	//画面インスタンスここまで
	Field now_field = nothing_field;
	Image back;
	Graphics buffer;
	boolean running = false;
	int character_size_x = 70;
	int character_size_y = 50;
	private Thread th;
	int Time = 9000;
	int player_x = screen_size_x / 2;
	int player_y = screen_size_y / 2;
	boolean is_next = false;//画面クリックorキーを押したら次のテキストを表示
	String text = "";
	Font mb100 = new Font("MS明朝", Font.BOLD, 100);
	Font mb50 = new Font("MS明朝", Font.BOLD, 50);
	Font mp20 = new Font("MS明朝", Font.PLAIN, 20);
	Font mp40 = new Font("MS明朝", Font.PLAIN, 40);
	Font mp15 = new Font("MS明朝", Font.PLAIN, 15);
	Font mp10 = new Font("MS明朝", Font.PLAIN, 10);
	Font mpi20 = new Font("MS明朝", Font.PLAIN | Font.ITALIC, 20);
	boolean is_show_text = false;
	boolean is_show_character = false;
	boolean is_character_in = true;
	String character_direct = "back";
	//※character_directについて-> backは下方向,frontはキャラクターから見て上方向であることに注意
	int item_icon_size = 50;//item-iconをdrawImageするときは50-1=49
	int show_bag_page = 1;//playerがshow-bagを利用してバッグの中身を見ているとき、そのページ数を保存 (1 ~)
	Button button_show_bag = new Button("show-bag [1]");
	Button button_show_hint = new Button("show-hint [2]");
	Button button_examine = new Button("examine [3]");
	Button button_show_trophy = new Button("show-trophy [4]");
	Button button_show_bag_prev = new Button("prev");
	Button button_show_bag_next = new Button("next");
	Button button_show_bag_close = new Button("close");//show-bag画面のcloseボタン
	Button button_show_trophy_close = new Button("close");//show-hint画面のcloseボタン
	//バッグにあるアイテム使用ボタン
	Button[] button_use_item = new Button[5];
	boolean is_show_buttons = false;
	boolean is_show_bag = false;
	boolean is_show_hint = false;
	boolean is_examine = false;
	boolean is_show_trophy = false;
	int button_size_x = 100;
	int button_size_y = 50;
	ArrayList<String> trophy = new ArrayList<String>();

	int vx = 20, vy = 20; //移動速度

	//キャラクター
	Image player_icon = null;
	Image c_f = null;
	Image c_l = null;
	Image c_b = null;
	Image c_r = null;
	//アイテムアイコン
	//west_room
	Image icon_dk_west_key = null;
	Image icon_flashlight = null;
	Image icon_magnet = null;
	Image icon_cutter = null;
	//dk
	Image icon_dk_jsr1_key = null;
	Image icon_hint = null;
	Image icon_jyuendama = null;
	Image icon_tap = null;
	Image icon_tunakan = null;
	//jsr2
	Image icon_tutu = null;
	Image icon_yukasita_key = null;
	//wc
	Image icon_datuijo_key = null;
	Image icon_triangle_hint = null;
	//その他
	Image title_gazo = null;//title用画像
	BufferedReader save_data = null;
	//ここまで画像
	ArrayList<String> message = new ArrayList<String>();//messageメッセージの保存
	int shown_message_length = 0;//実際に出力するメッセージの行数
	int shown_message_index = 0;//実際に出力するメッセージの先頭要素のindex(分割したメッセージの内一番上にくるメッセージのindex)
	boolean shown_message_first = true;
	int examine_range = 20; //examineで調べる範囲
	HashMap<String, Image> name_to_icon = new HashMap<String, Image>();//アイテム名にをkeyとしてiconを得る
	boolean screen_error = false;
	String screen_error_message = "";
	//バッグを既に確認しているか
	boolean is_already_checked_bag = false;

	//ex) examine_range= 50 -> 現地点 ~ 現地点から50ポイント離れた地点

	public void init() {
		//画面を500,500にセット
		setSize(screen_size_x, screen_size_y);
		for (int bIndex = 0;bIndex < button_use_item.length;bIndex++) {
			button_use_item[bIndex] = new Button("使う");
		}
		//マップ
		westernStyleRoom = new WesternStyleRoom(this);
		dk = new Dk(this);
		dkTop = new DkTop(this);
		entrance = new Entrance(this);
		japaneseStyleRoom1 = new JapaneseStyleRoom1(this);
		japaneseStyleRoom2 = new JapaneseStyleRoom2(this);
		datuijo = new Datuijo(this);
		bathroom = new Bathroom(this);
		wc = new WC(this);
		now_field = westernStyleRoom;
		now_field.setImages(getCodeBase());
		//キャラクター
		player_icon = getImage(getCodeBase(),"../material_data/escape_game/person/person_icon.png");
		c_f = getImage(getCodeBase(),"../material_data/escape_game/person/person_front.png");
		c_l = getImage(getCodeBase(),"../material_data/escape_game/person/person_left.png");
		c_b = getImage(getCodeBase(),"../material_data/escape_game/person/person_back.png");
		c_r = getImage(getCodeBase(),"../material_data/escape_game/person/person_right.png");
		//アイテムアイコン
		{
			//westroom
			icon_dk_west_key   = getImage(getCodeBase(),"../material_data/escape_game/itemicon/ICON_dk_westroom_key.png");
			icon_flashlight = getImage(getCodeBase(),"../material_data/escape_game/itemicon/ICON_flashlight.png");
			icon_magnet     = getImage(getCodeBase(),"../material_data/escape_game/itemicon/ICON_magnet.png");
			icon_cutter     = getImage(getCodeBase(),"../material_data/escape_game/itemicon/ICON_cutter.png");
			//dk
			icon_dk_jsr1_key = getImage(getCodeBase(),"../material_data/escape_game/itemicon/ICON_dk_jsr1_key.png");
			icon_hint = getImage(getCodeBase(),"../material_data/escape_game/itemicon/ICON_hint.png");
			icon_jyuendama = getImage(getCodeBase(),"../material_data/escape_game/itemicon/ICON_jyuendama.png");
			icon_tap = getImage(getCodeBase(),"../material_data/escape_game/itemicon/ICON_tap.png");
			icon_tunakan = getImage(getCodeBase(),"../material_data/escape_game/itemicon/ICON_tunakan.png");
			//jsr2
			icon_tutu = getImage(getCodeBase(), "../material_data/escape_game/itemicon/ICON_tutu.png");
			icon_yukasita_key = getImage(getCodeBase(), "../material_data/escape_game/itemicon/ICON_yukasita_key.png");
			//wc
			icon_datuijo_key = getImage(getCodeBase(), "../material_data/escape_game/itemicon/ICON_datuijo_key.png");
			icon_triangle_hint = getImage(getCodeBase(), "../material_data/escape_game/itemicon/ICON_triangle_hint.png");
			//その他
			title_gazo = getImage(getCodeBase(),"../material_data/escape_game/other/title_gazo.png");
		}
		//ここまで画像
		//調べる範囲を移動距離(vx)と同じにする。
		examine_range = vx;
		//各ArrayList/HashMapに要素を追加
		message.add("nothing");
		//west-room
		name_to_icon.put("洋室-DKの鍵", icon_dk_west_key);
		name_to_icon.put("懐中電灯", icon_flashlight);
		name_to_icon.put("可変式棒磁石", icon_magnet);
		name_to_icon.put("カッター", icon_cutter);
		//dk
		name_to_icon.put("DK-和室1の鍵", icon_dk_jsr1_key);
		name_to_icon.put("何かのヒント", icon_hint);
		name_to_icon.put("十円玉", icon_jyuendama);
		name_to_icon.put("蛇口", icon_tap);
		name_to_icon.put("ツナ缶", icon_tunakan);
		//japanese-style-room2
		name_to_icon.put("筒", icon_tutu);
		name_to_icon.put(IconName.YUKASITA_KEY, icon_yukasita_key);
		//wc
		name_to_icon.put(IconName.DK_DATUIJO_KEY, icon_datuijo_key);
		name_to_icon.put(IconName.TRIANGLE_HINT, icon_triangle_hint);
		//ここまで要素の追加
		back = createImage(screen_size_x+2000,screen_size_y+1000);
		buffer = back.getGraphics();
		add(button_show_bag);
		add(button_show_hint);
		add(button_examine);
		add(button_show_trophy);
		add(button_show_bag_prev);
		add(button_show_bag_next);
		add(button_show_bag_close);
		add(button_show_trophy_close);
		for (int bIndex = 0;bIndex < button_use_item.length;bIndex++) {
			add(button_use_item[bIndex]);
		}
		button_show_hint.addMouseListener(this);
		button_show_hint.addMouseMotionListener(this);
		button_show_hint.addKeyListener(this);
		button_show_bag.addMouseListener(this);
		button_show_bag.addMouseMotionListener(this);
		button_show_bag.addKeyListener(this);
		button_examine.addMouseListener(this);
		button_examine.addMouseMotionListener(this);
		button_examine.addKeyListener(this);
		button_show_trophy.addMouseListener(this);
		button_show_trophy.addMouseMotionListener(this);
		button_show_trophy.addKeyListener(this);
		button_show_bag_prev.addMouseListener(this);
		button_show_bag_prev.addMouseMotionListener(this);
		button_show_bag_prev.addKeyListener(this);
		button_show_bag_next.addMouseListener(this);
		button_show_bag_next.addMouseMotionListener(this);
		button_show_bag_next.addKeyListener(this);
		button_show_bag_close.addMouseListener(this);
		button_show_bag_close.addMouseMotionListener(this);
		button_show_bag_close.addKeyListener(this);
		button_show_trophy_close.addMouseListener(this);
		button_show_trophy_close.addMouseMotionListener(this);
		button_show_trophy_close.addKeyListener(this);
		for (int bIndex = 0;bIndex < button_use_item.length;bIndex++) {
			button_use_item[bIndex].addMouseListener(this);
			button_use_item[bIndex].addMouseMotionListener(this);
			button_use_item[bIndex].addKeyListener(this);
		}
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		requestFocus();
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		if (is_show_buttons == false) {
			button_show_bag.setLocation(screen_size_x + 2000, screen_size_y + 100);
			button_show_hint.setLocation(screen_size_x + 2000, screen_size_y + 100);
			button_examine.setLocation(screen_size_x + 2000, screen_size_y + 100);
			button_show_trophy.setLocation(screen_size_x + 2000, screen_size_y + 100);
			button_show_bag_prev.setLocation(screen_size_x + 2000, screen_size_y + 100);
			button_show_bag_next.setLocation(screen_size_x + 2000, screen_size_y + 100);
			button_show_bag_close.setLocation(screen_size_x + 2000, screen_size_y + 100);
			button_show_trophy_close.setLocation(screen_size_x + 2000, screen_size_y + 100);
			for (int bIndex = 0; bIndex < button_use_item.length; bIndex++) {
				button_use_item[bIndex].setLocation(screen_size_x + 2000, screen_size_y + 100);
			}
		}

		set_window();
		clean_setup();
		if (screenMode.equals("Title")) {
			//以下の１行でタイトル画像を表示
			buffer.drawImage(title_gazo, 0, 0, screen_size_x, screen_size_y, this);
			title.paint(this);
		} else if (screenMode.equals("Tuto")) {
			tuto.paint(this);
		} else if (screenMode.equals(now_field.toString())) {
			buffer.fillRect(0, 0, 500, 400);//余白を黒にする 例:datuijo
			show_map();
			show_me();
			now_field.paint(this);
			if (is_show_buttons) {
				button_show_bag.setLocation(0, screen_size_y - 100);
				button_show_bag.setSize(button_size_x, button_size_y);
				button_show_hint.setLocation(0 + button_size_x, screen_size_y - 100);
				button_show_hint.setSize(button_size_x, button_size_y);
				button_examine.setLocation(0 + button_size_x * 2, screen_size_y - 100);
				button_examine.setSize(button_size_x, button_size_y);
				button_show_trophy.setLocation(0 + button_size_x * 3, screen_size_y - 100);
				button_show_trophy.setSize(button_size_x, button_size_y);
				//messageメッセージを出力
				buffer.setFont(mp15);
				int line = -1;
				if (message.size() - 1 > 0) {
					buffer.drawImage(player_icon, 0, screen_size_y - 50, this);
					for (int i = 0; i < message.size() - 1; i++) {
						ArrayList<String> s_l = message_separate(message.get(i), 44);
						for (int j = 0; j < s_l.size(); j++) {
							if (i + j < shown_message_index)
								;
							else {
								line += 1;
								buffer.drawString(s_l.get(j), 60, screen_size_y - 35 + 15 * line);
							}
						}
					}
					if (shown_message_first) {
						shown_message_length = line;
						shown_message_first = false;
					}
				}
			}
		}

		if (is_show_buttons) {
			if (is_show_bag)
				show_bag();
			else if (is_show_hint)
				show_hint();
			else if (is_examine)
				examine("");
			else if (is_show_trophy)
				show_trophy();
		}

		if (screenMode.equals("Title")
				|| is_show_text == true)
			;
		else {
			clean_setup();
			show_current_time();
		}

		is_next = false;
		if (screen_error == true)
			show_screen_error();
		g.drawImage(back, 0, 0, this);
	}

	void show_current_time() {
		buffer.setColor(Color.white);
		buffer.fillRect(screen_size_x - 99, screen_size_y - 99, 99, 99);
		clean_setup();
		buffer.drawString("時間", screen_size_x - 80, screen_size_y - 50);
		buffer.drawString(Time / 1000 + " : 00", screen_size_x - 80, screen_size_y - 20);
		buffer.drawRect(0, screen_size_y - 100, screen_size_x, 100);
		buffer.drawRect(screen_size_x - 100, screen_size_y - 100, 100, 100);
		buffer.drawString("(" + player_x + "," + player_y + ")", 400, 420);
	}

	void show_bag() {//pressed show-bag button
		clean_setup();
		int sb_left = screen_size_x - (screen_size_x / 5) * 2 - (screen_size_x / 10);//show_bag画面の左地点
		int sb_width = (screen_size_x / 5) * 2;//show_bag画面の幅
		int sb_top = (screen_size_y / 10);//show_bag画面の上地点
		int sb_height = (screen_size_y / 5) * 3;//show_bag画面の高さ
		//show-bagの枠線とその内側を白色に塗り替えることでその部分の画面を初期化
		buffer.setColor(Color.white);
		buffer.fillRect(sb_left + 1, sb_top + 1, sb_width, sb_height - 2);
		buffer.setColor(Color.black);
		buffer.drawRect(sb_left, sb_top, sb_width, sb_height);
		//show-bag画面の内側を生成
		buffer.setFont(mp15);
		int separate_size = item_icon_size;
		int separate_number = sb_height / separate_size;//show-bag画面のサイズをicon-サイズに対応した値で割った数だけバック内容の枠を生成
		buffer.drawLine(sb_left + item_icon_size, sb_top //item-iconの枠線生成
				, sb_left + item_icon_size, sb_top + sb_height - separate_size);
		for (int i = 0; i < separate_number; i++) {
			buffer.drawLine(sb_left//item詳細の枠線生成
					, sb_top + (sb_height / separate_number) * i, sb_left + sb_width,
					sb_top + (sb_height / separate_number) * i);
		}
		//bagのアイテムを表示(表示page*5+1~5)
		if (bag.size() != 0) {
			for (int i = (show_bag_page - 1) * 5; i < (show_bag_page - 1) * 5 + 5; i++) {
				if (i >= bag.size()) { //bag内のアイテム数がアイテムナンバー(ページ数*5+i)以下-> (1-1)*5+i>= アイテム数0
					break;
				} else {
					int item_index = i;
					buffer.drawImage(name_to_icon.get(bag.get(item_index)), sb_left + 1,
							sb_top + item_icon_size * (i % 5) + 1, item_icon_size - 1, item_icon_size - 1, this);
					buffer.drawString(bag.get(item_index), sb_left + item_icon_size + 5,
							(sb_top + separate_size * (i % 5) + 15));
					//使うボタン
					button_use_item[i % 5].setLocation(sb_left + sb_width - button_use_item[i % 5].getWidth(),
							sb_top + separate_size * (i % 5) + 25);
				}
			}
		} else {
			buffer.drawString("nothing in the bag", sb_left + item_icon_size + 10, sb_top + (separate_size / 10) * 6);
		}
		buffer.drawString(show_bag_page + "/" + ((bag.size() / separate_number) + 1)//page-index
				, sb_left + 10,
				sb_top + (sb_height / separate_number) * (separate_number - 1) + (separate_size / 10) * 6);
		button_show_bag_prev.setLocation //prevボタンセット
		(sb_left + (sb_width - 10) / 4,
				sb_top + (sb_height / separate_number) * (separate_number - 1) + (separate_size / 10) * 6 - 10);
		button_show_bag_next.setLocation //nextボタンセット
		(sb_left + (sb_width - 10) / 4 * 2,
				sb_top + (sb_height / separate_number) * (separate_number - 1) + (separate_size / 10) * 6 - 10);
		button_show_bag_close.setLocation //closeボタンセット
		(sb_left + (sb_width - 10) / 4 * 3,
				sb_top + (sb_height / separate_number) * (separate_number - 1) + (separate_size / 10) * 6 - 10);
		clean_setup();
	}

	void show_hint() {//pressed show-hint button
		String hint = now_field.hint();
		message_add(hint);
		is_show_hint = false;
	}

	void examine(String item) {//pressed examine button
		//プレイヤーがいる地点から、プレイヤーの向いている方向50ポイント分の座標を調べて、
		//今の地点と違うオブジェクトがあった場合はそのオブジェクト名をexamineの結果として取得する。
		String here_now = now_field.here(player_x, player_y);
		String examine_point = here_now;//今いるオブジェクトと同じ値で初期化
		if (character_direct.equals("back")) {
			for (int i = 1; i <= examine_range; i++) {
				examine_point = now_field.here(player_x, player_y + i);
				if (here_now.equals(examine_point))
					;//今いる地点と同じなら何もしない
				else {//今いる地点と違うオブジェクトを発見
					now_field.examine_effect(examine_point, item);
					break;
				}
			}
		} else if (character_direct.equals("left")) {
			for (int i = 1; i <= examine_range; i++) {
				examine_point = now_field.here(player_x - i, player_y);
				if (here_now.equals(examine_point))
					;//今いる地点と同じなら何もしない
				else {//今いる地点と違うオブジェクトを発見
					now_field.examine_effect(examine_point, item);
					break;
				}
			}
		} else if (character_direct.equals("front")) {
			for (int i = 1; i <= examine_range; i++) {
				examine_point = now_field.here(player_x, player_y - i);
				if (here_now.equals(examine_point))
					;//今いる地点と同じなら何もしない
				else {//今いる地点と違うオブジェクトを発見
					now_field.examine_effect(examine_point, item);
					break;
				}
			}
		} else if (character_direct.equals("right")) {
			for (int i = 1; i <= examine_range; i++) {
				examine_point = now_field.here(player_x + i, player_y);
				if (here_now.equals(examine_point))
					;//今いる地点と同じなら何もしない
				else {//今いる地点と違うオブジェクトを発見
					now_field.examine_effect(examine_point, item);
					break;
				}
			}
		}
		clean_setup();
		buffer.drawString("here:     " + here_now, 250, 370);
		buffer.drawString("examine:" + examine_point, 250, 390);
		now_field.examine_result(examine_point);
		is_examine = false;
	}

	void show_trophy() {//pressed show-trophy button
		clean_setup();
		//show-trophyの枠線とその内側を白色に塗り替えることでその部分の画面を初期化
		buffer.setColor(Color.white);
		buffer.fillRect(screen_size_x - (screen_size_x / 5) * 2 - (screen_size_x / 10) + 1, (screen_size_y / 10),
				(screen_size_x / 5) * 2 - 2, (screen_size_y / 5) * 3 - 2);
		buffer.setColor(Color.black);
		int st_left = screen_size_x - (screen_size_x / 5) * 2 - (screen_size_x / 10);//show_trophy画面の左地点
		int st_width = (screen_size_x / 5) * 2;//show_trophy画面の幅
		int st_top = (screen_size_y / 10);//show_trophy画面の上地点
		int st_height = (screen_size_y / 5) * 3;//show_trophy画面の高さ
		buffer.drawRect(st_left, st_top, st_width, st_height);
		buffer.drawLine(st_left + item_icon_size, st_top + 20, st_left + item_icon_size,
				st_top + item_icon_size * 4 + 20);//仕切り縦線
		for (int i = 0; i < 5; i++) {
			buffer.drawLine(st_left, st_top + item_icon_size * i + 20, st_left + st_width,
					st_top + item_icon_size * i + 20);//仕切り横線
		}
		buffer.setFont(mp15);
		buffer.drawString("Show  you  got  trophies", st_left + 20, st_top + 15);
		buffer.drawString("<達成率>", st_left + 50, st_top + 220 + 20);
		buffer.setFont(mp40);
		buffer.drawString(0 + "", st_left + 50, st_top + 220 + 50);
		buffer.setFont(mp15);
		buffer.drawString("%", st_left + 80, st_top + 220 + 50);
		button_show_trophy_close.setLocation //closeボタンセット
		(st_left + st_width * 2 / 3,
				st_top + 220 + 30);
	}

	ArrayList<String> message_separate(String statement, int s_size) {
		//渡された文字列を指定された文字サイズ以下に分割してその結果を配列で返す
		//全角文字は3byte->サイズ2に変換
		//半角文字は1byte->サイズ1に変換
		char[] char_array = statement.toCharArray();
		ArrayList<String> statement_list = new ArrayList<String>();
		int new_array_index = -1;//新しく生成する文字列のインデックス
		int new_array_total_size = 0;//今生成している文字列の合計文字サイズ
		for (int i = 0; i < char_array.length; i++) {
			String a = char_array[i] + "";
			int char_size = 0;
			if (a.getBytes().length == 1)
				char_size = 1;
			else
				char_size = 2;
			if (new_array_total_size + char_size > s_size //文字列のサイズがs_sizeを越えた時、次の文字列を生成
					|| new_array_index == -1) {
				statement_list.add(a);
				new_array_total_size = char_size;
				new_array_index += 1;
			} else {
				statement_list.set(
						new_array_index, //インデックス
						statement_list.get(new_array_index) + a);//今の文字列と新しい文字を結合
				new_array_total_size += char_size;
			}
		}
		return statement_list;
	}

	/**
	 * font-sizeと描画色の初期化
	 */
	void clean_setup() {
		buffer.setFont(mp20);//font-sizeを20に初期化
		buffer.setColor(Color.black);//描画色を黒に初期化
	}

	/**
	 * プログラムレベルのエラー表示
	 */
	void show_screen_error() {
		clean_setup();
		buffer.setColor(Color.red);
		buffer.drawString(screen_error_message, 100, 450);
	}

	public void set_window() {
		buffer.setColor(Color.black);
		buffer.fillRect(0, 0, screen_size_x + 2000, screen_size_x + 1000);
		buffer.setColor(Color.white);
		buffer.fillRect(0, 0, screen_size_x, screen_size_y);
		buffer.setColor(Color.black);
	}

	public void show_map() {
		if (screenMode.equals(now_field.toString())) {
			now_field.showMap(this);
		}
	}

	public void show_me() {
		if (character_direct.equals("front"))
			buffer.drawImage(c_f, player_x, player_y, character_size_x, character_size_y, this);
		else if (character_direct.equals("left"))
			buffer.drawImage(c_l, player_x, player_y, character_size_x, character_size_y, this);
		else if (character_direct.equals("back"))
			buffer.drawImage(c_b, player_x, player_y, character_size_x, character_size_y, this);
		else if (character_direct.equals("right"))
			buffer.drawImage(c_r, player_x, player_y, character_size_x, character_size_y, this);
	}

	void clear_show_button() {
		button_show_bag_prev.setLocation(screen_size_x + 2000, screen_size_y + 100);
		button_show_bag_next.setLocation(screen_size_x + 2000, screen_size_y + 100);
		button_show_bag_close.setLocation(screen_size_x + 2000, screen_size_y + 100);
		button_show_trophy_close.setLocation(screen_size_x + 2000, screen_size_y + 100);
		for (int bIndex = 0; bIndex < button_use_item.length; bIndex++) {
			button_use_item[bIndex].setLocation(screen_size_x + 2000, screen_size_y + 100);
		}
	}

	void clear_show_window() {//showしているwindowをclose
		is_show_bag = false;
		is_show_hint = false;
		is_examine = false;
		is_show_trophy = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (running == false) {
			running = true;
			th = new Thread(this);
			th.start();
		}
		is_next = true;
		screen_error = false;
		Point point = e.getPoint();
		if (shown_message_index < shown_message_length - 2) {
			//出力されるメッセージの先頭要素のindex値が分割されたメッセージの行数未満
			shown_message_index++;
		} else {
			message_clear();
			clear_show_button();
			if (e.getSource() == button_show_bag && is_show_buttons == true) {//click show-bag
				func_pressed_show_bag_button();
			} else if (e.getSource() == button_show_hint && is_show_buttons == true) {//click show-hint
				func_pressed_show_hint_button();
			} else if (e.getSource() == button_examine && is_show_buttons == true) {//click examine
				func_pressed_examine_button();
			} else if (e.getSource() == button_show_trophy && is_show_buttons == true) {//click show-trophy
				func_pressed_show_trophy_button();
			} else if (is_show_bag && e.getSource() == button_show_bag_prev) {//click prev(on show-bag window)
				if ((show_bag_page - 1) * 5 - 1 >= 0) {
					show_bag_page--;
				}
			} else if (is_show_bag && e.getSource() == button_show_bag_next) {//click next(on show-bag window)
				if (show_bag_page * 5 <= bag.size() - 1) {//bagの中に6このアイテム-> size=6(max-index=5),page1->2 =>1(show_bag_page)*5=5
					show_bag_page++;
				}
			} else if (is_show_bag && e.getSource() == button_show_bag_close) {//click close(on show-bag window)
				clear_show_window();
				is_show_bag = false;
			} else if (is_show_trophy && e.getSource() == button_show_trophy_close) {//click close(on show-hint window)
				clear_show_window();
				is_show_trophy = false;
			} else if (is_show_bag) {
				//show-bag画面を開いている状態で、使うボタンを押す
				for (int bIndex = 0; bIndex < button_use_item.length; bIndex++) {
					if (e.getSource() == button_use_item[bIndex]) {
						//itemのindex
						int bag_index = bIndex + (show_bag_page - 1) * button_use_item.length;
						if (isItemStatusChanged(bag_index)) {
							break;
						}
						examine(bag.get(bag_index));
						clear_show_window();
						is_show_bag = false;
						break;
					}
				}
			} else if (screenMode.equals("Title")) {
				if (title.click_where(point.x, point.y) == "first") {
					is_show_text = true;
					//tuto画面に変更
					screenMode = "Tuto";
					//tutoテキストを追加
					tuto.text_index = 0;
					text = tuto.return_text();
				} else if (title.click_where(point.x, point.y) == "continue") {
					boolean is_comp = read_savedata();
					if (is_comp == false) {
						screen_error = true;
						screen_error_message = "データを読み込めませんでした！！";
					}
				}
			} else if (screenMode.equals("Tuto")) {
				if (tuto.final_text() == true) {
					is_show_character = true;
					screenMode = "WesternStyleRoom";
					now_field.text_index = 0;
					text = now_field.texts.get(now_field.text_index);
				} else {
					tuto.text_index++;
					text = tuto.return_text();
				}
			} else if (screenMode.equals("WesternStyleRoom")) {
				if (is_show_text == true) {
					if (now_field.final_text() == true) {
						is_show_text = false;
						is_show_buttons = true;
					} else {
						now_field.text_index++;
						text = now_field.texts.get(now_field.text_index);
					}
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (running == false) {
			running = true;
			th = new Thread(this);
			th.start();
		}
		is_next = true;
		screen_error = false;
		/*
		 * チュートリアル時の処理
		 */
		if (screenMode.equals("Tuto")) {
			if (tuto.final_text() == true) {
				is_show_character = true;
				screenMode = "WesternStyleRoom";
				now_field = westernStyleRoom;
				now_field.text_index = 0;
				text = now_field.texts.get(now_field.text_index);
			} else {
				tuto.text_index++;
				text = tuto.return_text();
			}
		}
		/*
		 * 洋室時の処理
		 */
		else if (screenMode.equals("WesternStyleRoom")) {
			if (is_show_text == true) {
				if (now_field.final_text() == true) {
					is_show_text = false;
					is_show_buttons = true;
				} else {
					now_field.text_index++;
					text = now_field.texts.get(now_field.text_index);
				}
			}
		}
		/*
		 * DKの処理
		 */

		if (shown_message_index < shown_message_length - 2) {
			//出力されるメッセージの先頭要素のindex値が分割されたメッセージの行数未満
			shown_message_index++;
		} else {
			message_clear();
			clear_show_button();
			int n = e.getKeyCode();
			switch (n) {
			//キー下矢印(↓)
			case KeyEvent.VK_DOWN:
				clear_show_window();
				if (is_show_character == true) {
					if (screenMode.equals(now_field.toString())) {
						if (character_direct.equals("back")) {
							if (now_field.is_character_in(player_x, player_y + vy)) {
								player_y += vy;
								/*
								 * 部屋移動の処理
								 */
								if (10 <= player_x && player_x <= 410 && 380 <= player_y && now_field == dkTop) {
									/* DkTop -> Dk */
									now_field = dk;
									screenMode = "Dk";
									now_field.setImages(getCodeBase());
									player_y = -10;
								} else if (150 <= player_x && player_x <= 310 && 340 <= player_y
										&& now_field == entrance) {
									/* Entrance -> DkTop */
									now_field = dkTop;
									screenMode = "DkTop";
									now_field.setImages(getCodeBase());
									player_x = 190;
									player_y = 80;

								} else if (now_field == wc && now_field.here(player_x, player_y).equals(WC.DOOR_TO_DK)) {
									/* WC -> DkTop */
									now_field = dkTop;
									screenMode = "DkTop";
									now_field.setImages(getCodeBase());
									player_x = 350;
									player_y = 110;

								} else if (170 <= player_x && player_x <= 230 && 310 <= player_y && now_field == dk) {
									/* Dk -> westroom */
									now_field = westernStyleRoom;
									screenMode = "WesternStyleRoom";
									now_field.setImages(getCodeBase());
									player_x = 40;
									player_y = 30;
								} else if (10 <= player_x && player_x <= 70 && 310 <= player_y && now_field == dk
										&& dk.isDoor_wasitu1()) {
									/* Dk -> JapaneseStyleRoom1 */
									now_field = japaneseStyleRoom1;
									screenMode = "JapaneseStyleRoom1";
									now_field.setImages(getCodeBase());
									player_x = 300;
									player_y = 30;
								} else if (now_field == japaneseStyleRoom2 && now_field.here(player_x, player_y)
										.equals(JapaneseStyleRoom2.getDoorToJSR1())) {
									// JSR2 -> JSR1
									now_field = japaneseStyleRoom1;
									screenMode = "JapaneseStyleRoom1";
									now_field.setImages(getCodeBase());
									player_x = 100;
									player_y = 50;
								}
							}
						} else {
							if (character_direct.equals("left")
									|| character_direct.equals("right")) {
								int px = (character_size_x - character_size_y) / 2 + player_x;
								int py = (character_size_y - character_size_x) / 2 + player_y;
								int csx = character_size_x;
								int csy = character_size_y;
								character_size_x = csy;
								character_size_y = csx;
								if (now_field.is_character_in(px, py)) {
									player_x = px;
									player_y = py;
									character_direct = "back";
								} else {
									character_size_x = csx;
									character_size_y = csy;
									message_add("おっと、少し下がらないと周りに当たってまう..");
									if (character_direct.equals("right")) {
										if (now_field.is_character_in(player_x - 10, player_y))
											player_x -= 10;
										else
											message_add("あぁ...、後ろもさがれねぇわ...");
									}
									if (character_direct.equals("left")) {
										if (now_field.is_character_in(player_x + 10, player_y))
											player_x += 10;
										else
											message_add("あぁ...、後ろもさがれねぇわ...");
									}
								}
							} else if (character_direct.equals("front"))
								character_direct = "back";
						}
					}
				}
				break;
			//キー左矢印(←)
			case KeyEvent.VK_LEFT:
				clear_show_window();
				if (is_show_character == true) {
					if (screenMode.equals(now_field.toString())) {
						if (character_direct.equals("left")) {
							if (now_field.is_character_in(player_x - vx, player_y)) {
								player_x -= vx;
								/*
								 * 部屋移動の処理
								 */
								if (player_x < 120 && 180 <= player_y && player_y <= 220 && now_field == dkTop) {
									/* DkTop -> Datuijo */
									now_field = datuijo;
									screenMode = "Datuijo";
									now_field.setImages(getCodeBase());
									player_x = 330;
									player_y = 260;
								} else if (player_x < 130 && 200 <= player_y && player_y <= 240
										&& now_field == datuijo) {
									/* datuijo -> Bathroom */
									now_field = bathroom;
									screenMode = "Bathroom";
									now_field.setImages(getCodeBase());
									player_x = 330;
									player_y = 260;
								} else if (now_field == dk && player_x < 30 && 40 <= player_y && player_y <= 110) {
									/* DK -> JapaneseStyleRoom2 */
									now_field = japaneseStyleRoom2;
									screenMode = "JapaneseStyleRoom2";
									now_field.setImages(getCodeBase());
									player_x = 330;
									player_y = 100;
								}
							}
						} else {
							if (character_direct.equals("front")
									|| character_direct.equals("back")) {
								int px = (character_size_x - character_size_y) / 2 + player_x;
								int py = (character_size_y - character_size_x) / 2 + player_y;
								int csx = character_size_x;
								int csy = character_size_y;
								character_size_x = csy;
								character_size_y = csx;
								if (now_field.is_character_in(px, py)) {
									player_x = px;
									player_y = py;
									character_direct = "left";
								} else {
									character_size_x = csx;
									character_size_y = csy;
									message_add("おっと、少し下がらないと周りに当たってまう..");
									if (character_direct.equals("back")) {
										if (now_field.is_character_in(player_x, player_y - 10))
											player_y -= 10;
										else
											message_add("あぁ...、後ろもさがれねぇわ...");
									}
									if (character_direct.equals("front")) {
										if (now_field.is_character_in(player_x, player_y + 10))
											player_y += 10;
										else
											message_add("あぁ...、後ろもさがれねぇわ...");
									}
								}
							} else if (character_direct.equals("right"))
								character_direct = "left";
						}
					}
				}
				break;
			//キー上矢印(↑)
			case KeyEvent.VK_UP:
				clear_show_window();
				if (is_show_character == true) {
					if (screenMode.equals(now_field.toString())) {
						if (character_direct.equals("front")) {
							if (now_field.is_character_in(player_x, player_y - vy)) {
								player_y -= vy;
								/*
								 * 部屋移動の処理
								 */
								if (10 <= player_x && player_x <= 80 && player_y <= 0
										&& westernStyleRoom.get_is_open_door() == true
										&& now_field == westernStyleRoom) {
									/* westroom -> Dk */
									now_field = dk;
									screenMode = "Dk";
									now_field.setImages(getCodeBase());
									player_x = 190;
									player_y = 300;
								} else if (10 <= player_x && player_x <= 410 && player_y <= -30 && now_field == dk) {
									/* Dk -> DkTop */
									now_field = dkTop;
									screenMode = "DkTop";
									now_field.setImages(getCodeBase());
									player_y = 370;
								} else if (150 <= player_x && player_x <= 230 && player_y <= 50 && now_field == dkTop) {
									/* DkTop -> Entrance */
									now_field = entrance;
									screenMode = "Entrance";
									now_field.setImages(getCodeBase());
									player_y = 300;
								} else if (340 < player_x && player_x <= 370 && player_y < 80 && now_field == dkTop) {
									/* DkTop -> WC */
									now_field = wc;
									screenMode = "WC";
									now_field.setImages(getCodeBase());
									player_x = 50;
									player_y = 300;
								} else if (280 <= player_x && player_x <= 320 && player_y <= 20
										&& now_field == japaneseStyleRoom1) {
									// JSRoom1 -> Dk
									now_field = dk;
									screenMode = "Dk";
									now_field.setImages(getCodeBase());
									player_x = 40;
									player_y = 300;
								} else if (50 <= player_x && player_x <= 120 && player_y <= 20
										&& now_field == japaneseStyleRoom1) {
									// JSRoom1 -> JSRoom2
									//ドアの開閉フラグ変更
									japaneseStyleRoom1.changeDoorStatusToJSR2();
									japaneseStyleRoom2.updateJSR1DoorFlag(japaneseStyleRoom1.getIsJSRoom2DoorOpened());

									now_field = japaneseStyleRoom2;
									screenMode = "JapaneseStyleRoom2";
									now_field.setImages(getCodeBase());
									player_x = 320;
									player_y = 300;
								}
							}
						} else {
							if (character_direct.equals("left")
									|| character_direct.equals("right")) {
								int px = (character_size_x - character_size_y) / 2 + player_x;
								int py = (character_size_y - character_size_x) / 2 + player_y;
								int csx = character_size_x;
								int csy = character_size_y;
								character_size_x = csy;
								character_size_y = csx;
								if (now_field.is_character_in(px, py)) {
									player_x = px;
									player_y = py;
									character_direct = "front";
								} else {
									character_size_x = csx;
									character_size_y = csy;
									message_add("おっと、少し下がらないと周りに当たってまう..");
									if (character_direct.equals("right")) {
										if (now_field.is_character_in(player_x - 10, player_y))
											player_x -= 10;
										else
											message_add("あぁ...、後ろもさがれねぇわ...");
									}
									if (character_direct.equals("left")) {
										if (now_field.is_character_in(player_x + 10, player_y))
											player_x += 10;
										else
											message_add("あぁ...、後ろもさがれねぇわ...");
									}
								}
							} else if (character_direct.equals("back"))
								character_direct = "front";
						}
					}
				}
				break;
			//キー右矢印(→)
			case KeyEvent.VK_RIGHT:
				clear_show_window();
				if (is_show_character == true) {
					if (screenMode.equals(now_field.toString())) {
						if (character_direct.equals("right")) {
							if (now_field.is_character_in(player_x + vx, player_y)) {
								player_x += vx;
								/*
								 * 部屋移動の処理
								 */
								if (350 <= player_x && 230 <= player_y && player_y <= 280 && now_field == datuijo) {
									/* datuijo -> DkTop */
									now_field = dkTop;
									screenMode = "DkTop";
									now_field.setImages(getCodeBase());
									player_x = 130;
									player_y = 200;
								} else if (now_field == japaneseStyleRoom2 && now_field.here(player_x, player_y)
										.equals(JapaneseStyleRoom2.getDoorToDK())) {
									// JSR2 -> DK
									// DK側のドア開閉フラグを変更
									dk.setDoor_wasitu2(true);

									now_field = dk;
									screenMode = "Dk";
									now_field.setImages(getCodeBase());
									player_x = 40;
									player_y = 70;
								}
							}
						} else {
							if (character_direct.equals("front")
									|| character_direct.equals("back")) {
								int px = (character_size_x - character_size_y) / 2 + player_x;
								int py = (character_size_y - character_size_x) / 2 + player_y;
								int csx = character_size_x;
								int csy = character_size_y;
								character_size_x = csy;
								character_size_y = csx;
								if (now_field.is_character_in(px, py)) {
									player_x = px;
									player_y = py;
									character_direct = "right";
								} else {
									message_add("おっと、少し下がらないと周りに当たってまう..");
									character_size_x = csx;
									character_size_y = csy;
									if (character_direct.equals("back")) {
										if (now_field.is_character_in(player_x, player_y - 10))
											player_y -= 10;
										else
											message_add("あぁ...、後ろもさがれねぇわ...");
									}
									if (character_direct.equals("front")) {
										if (now_field.is_character_in(player_x, player_y + 10))
											player_y += 10;
										else
											message_add("あぁ...、後ろもさがれねぇわ...");
									}
								}
							} else if (character_direct.equals("left"))
								character_direct = "right";
						}
					}
				}
				break;
			/*
			 * key1を押すことでボタンshow-bagの機能を呼び出す
			 */
			case KeyEvent.VK_1:
				if (screenMode.equals("Title")) { //はじめから
					is_show_text = true;
					//tuto画面に変更
					screenMode = "Tuto";
					//tutoテキストを追加
					tuto.text_index = 0;
					text = tuto.return_text();
				} else { //title画面以外ではshow_bagボタンの処理
					func_pressed_show_bag_button();
				}
				break;
			/*
			 * key2を押すことでボタンshow-hintの機能を呼び出す
			 */
			case KeyEvent.VK_2:
				if (screenMode.equals("Title")) { //つづきから
					boolean is_comp = read_savedata();
					if (is_comp == false) {
						screen_error = true;
						screen_error_message = "データを読み込めませんでした！！";
					}
				} else { //titlw画面以外ではshow_hintボタンの処理
					func_pressed_show_hint_button();
				}
				break;
			/*
			 * key3を押すことでボタンexamineの機能を呼び出す
			 */
			case KeyEvent.VK_3:
				func_pressed_examine_button();
				break;
			/*
			 * key2を押すことでボタンshow-trophyの機能を呼び出す
			 */
			case KeyEvent.VK_4:
				func_pressed_show_trophy_button();
				break;
			/*
			 * Escapeキーを押すことで今開いているウィンドウを閉じる
			 * （この機能は
			 * バッグ画面,トロフィー画面に有効）
			 */
			case KeyEvent.VK_ESCAPE:
				clear_show_window();
				break;
			case KeyEvent.VK_F1:
				if (is_show_bag) {
					int bag_index = 0 + (show_bag_page - 1) * button_use_item.length;
					if (bag_index <= bag.size() - 1) {//対象のindexにアイテムが存在しているとき
						if (isItemStatusChanged(bag_index)) {
							break;
						}
						examine(bag.get(bag_index));
						clear_show_window();
						is_show_bag = false;
					}
				}
				break;
			case KeyEvent.VK_F2:
				if (is_show_bag) {
					int bag_index = 1 + (show_bag_page - 1) * button_use_item.length;
					if (bag_index <= bag.size() - 1) {//対象のindexにアイテムが存在しているとき
						if (isItemStatusChanged(bag_index)) {
							break;
						}
						examine(bag.get(bag_index));
						clear_show_window();
						is_show_bag = false;
					}
				}
				break;
			case KeyEvent.VK_F3:
				if (is_show_bag) {
					int bag_index = 2 + (show_bag_page - 1) * button_use_item.length;
					if (bag_index <= bag.size() - 1) {//対象のindexにアイテムが存在しているとき
						if (isItemStatusChanged(bag_index)) {
							break;
						}
						examine(bag.get(bag_index));
						clear_show_window();
						is_show_bag = false;
					}
				}
				break;
			case KeyEvent.VK_F4:
				if (is_show_bag) {
					int bag_index = 3 + (show_bag_page - 1) * button_use_item.length;
					if (bag_index <= bag.size() - 1) {//対象のindexにアイテムが存在しているとき
						if (isItemStatusChanged(bag_index)) {
							break;
						}
						examine(bag.get(bag_index));
						clear_show_window();
						is_show_bag = false;
					}
				}
				break;
			case KeyEvent.VK_F5:
				if (is_show_bag) {
					int bag_index = 4 + (show_bag_page - 1) * button_use_item.length;
					if (bag_index <= bag.size() - 1) {//対象のindexにアイテムが存在しているとき
						if (isItemStatusChanged(bag_index)) {
							break;
						}
						examine(bag.get(bag_index));
						clear_show_window();
						is_show_bag = false;
					}
				}
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	/*
	 * show-bagボタンがクリックされたときの処理、
	 * およびshow-bagボタンに対応するkeyが押されたときの処理をこの関数を呼ぶことで実行
	 */
	void func_pressed_show_bag_button() {
		if (is_already_checked_bag == false) { //初めてバッグを開いたとき
			is_already_checked_bag = true;
			message_add("あれ、よく見たらバッグを持ってたわ。");
			message_add("気になったものがあればこれにいれよう。");
		}
		if (is_show_bag == true) {
			is_show_bag = false;
			clear_show_window();
		} else {
			clear_show_window();
			is_show_bag = true;
			show_bag_page = 1;
		}
	}

	/*
	 * show-hintボタンがクリックされたときの処理、
	 * およびshow-hintボタンに対応するkeyが押されたときの処理をこの関数を呼ぶことで実行
	 */
	void func_pressed_show_hint_button() {
		clear_show_window();
		is_show_hint = true;
	}

	/*
	 * examineボタンがクリックされたときの処理、
	 * およびexamineボタンに対応するkeyが押されたときの処理をこの関数を呼ぶことで実行
	 */
	void func_pressed_examine_button() {
		clear_show_window();
		is_examine = true;
	}

	/*
	 * show-trophyボタンがクリックされたときの処理、
	 * およびshow-trophyボタンに対応するkeyが押されたときの処理をこの関数を呼ぶことで実行
	 */
	void func_pressed_show_trophy_button() {
		if (is_show_trophy == true) {
			is_show_trophy = false;
			clear_show_window();
		} else {
			clear_show_window();
			is_show_trophy = true;
		}
	}

	void message_clear() {
		for (int i = message.size() - 1; i >= 0; i--) {
			if (message.get(i).equals("nothing"))
				;
			else
				message.remove(i);
		}
		shown_message_length = 0;
		shown_message_index = 0;
		shown_message_first = true;
	}

	boolean exist_item(String item) {//引数で渡されたitemと同じものがバックにあるか
		for (int i = 0; i < bag.size(); i++) {
			if (bag.get(i).equals(item)) {
				return true;
			}
		}
		return false;
	}

	void message_add(String e1) {
		//新しく追加するエラーメッセージをエラーメッセ―ジの最終要素(nothingの一つ前)に追加する
		int last_index = message.size() - 1;
		if (message.get(last_index).equals("nothing")) {
			message.set(last_index, e1);
			message.add("nothing");
		}
	}

	//アイテム使用によって、ほかのアイテムに影響が及ぶ場合はその処理を行いtrueを返す
	boolean isItemStatusChanged(int index) {
		if (bag.get(index).equals(IconName.CUTTER)) {
			for (int targetItemIndex = 0; targetItemIndex < bag.size(); targetItemIndex++) {
				if (bag.get(targetItemIndex).equals(IconName.TUTU)) {
					message_add("カッターで筒を切ってみよう");
					message_add("お、中から鍵が出てきたぞ！！");
					message_add("(鍵を手に入れた！！)");
					bag.add(IconName.YUKASITA_KEY);
					bag.remove(targetItemIndex);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void run() {
		try {
			while (true) {
				repaint();
				if (is_show_text == true)
					;
				else
					Time = (Time + 2) % 24000;
				Thread.sleep(10);
			}
		} catch (Exception e) {
			System.out.println("fail with run method");
		}
	}

	boolean read_savedata() {
		try {
			save_data = new BufferedReader(new FileReader("../savedata/escape_game/savedata.txt"));
			String data = save_data.readLine();
			while (data != null) {
				if (data.substring(0, 2).equals("//")) { //コメント行
				} else if (data.substring(0, 3).equals("END")) { //セーブファイル終端行
					setConfig();
				} else {
					String[] dataArray = data.split(":"); //データラベル:データ
					String dataLabel = dataArray[0]; //データラベル
					String dataContent = dataArray[1]; //データ
					String[] dataLabels = dataLabel.split("\\("); //データラベルを"("で分解[※データラベル(部屋名)となっているものがあるため]※(等をsplitする際はescape記号必須
					String dataLabel_woLP = null; //woLP = without left Paren
					if (dataLabels != null)
						dataLabel_woLP = dataLabels[0];
					if (dataLabel.equals("playerX")) {
						player_x = Integer.parseInt(dataContent);
					} else if (dataLabel.equals("playerY")) {
						player_y = Integer.parseInt(dataContent);
					} else if (dataLabel.equals("field")) {
						Field field = getField(dataContent);
						if (field == null) { //field取得失敗
							System.out.println("fieldの取得に失敗しました。");
							return false;
						}
						now_field = field;
						screenMode = dataContent;
						now_field.setImages(getCodeBase());
					} else if (dataLabel.equals("item")) {
						String[] items = dataContent.split(",");
						for (int index = 0; index < items.length; index++) {
							bag.add(items[index]);
						}
					} else if (dataLabel_woLP.equals("flag")) {
						String[] fieldNames = dataLabels[1].split("\\)"); //field名)を")"で分解
						String fieldName = fieldNames[0]; //room名
						if (fieldName.equals("Common")) { //共通のフラグ
							String[] flags = dataContent.split(",");
							for (int index = 0; index < flags.length; index++) {
								if (flags[index].equals("nothing"))
									break;
								this.setFlagTrue(flags[index]);
							}
						} else { //各フィールド(部屋)毎のフラグ
							Field field = getField(fieldName);
							String[] flags = dataContent.split(",");
							for (int index = 0; index < flags.length; index++) {
								if (flags[index].equals("nothing"))
									break;
								field.setFlagTrue(flags[index]);
							}
						}
					} else { //設定されていないラベルがファイルに書かれている場合
						System.out.println("設定されていないラベル名がファイルに記述されています");
						return false;
					}
				}
				data = save_data.readLine();
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * field名を示す文字列から、部屋名を解析しfieldインスタンスを返す
	 * @param fieldName
	 * @return
	 */
	Field getField(String fieldName) {
		if (fieldName.equals("WesternStyleRoom"))
			return westernStyleRoom;
		else if (fieldName.equals("Dk"))
			return dk;
		else if (fieldName.equals("DkTop"))
			return dkTop;
		else if (fieldName.equals("Entrance"))
			return entrance;
		else if (fieldName.equals("JapaneseStyleRoom1"))
			return japaneseStyleRoom1;
		else if (fieldName.equals("JapaneseStyleRoom2"))
			return japaneseStyleRoom2;
		else if (fieldName.equals("Datuijo"))
			return datuijo;
		else if (fieldName.equals("WC"))
			return wc;
		else
			return null;
	}

	/**
	 * "つづきから"を選択した場合の設定を行う
	 */
	void setConfig() {
		is_show_character = true;
		is_show_buttons = true;
	}

	/**
	 * フラグをtrueにする関数
	 * @param flagString
	 */
	void setFlagTrue(String flagString) {
		if (flagString.equals("is_already_checked_bag"))
			is_already_checked_bag = true;
		else
			System.out.println("次のフラグ名に対応するフラグはありませんでした:" + flagString);
	}

}