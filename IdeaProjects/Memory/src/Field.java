import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import static javax.swing.JOptionPane.showMessageDialog;

class Field extends JFrame{

    private JPanel about;

    private JLabel inAnswer;
    private JLabel points;
    private JLabel pointsOne;
    private JLabel round;

    private JButton start;
    private JButton check;

    private JSpinner levelNumber;
    private JSpinner speedNumber;

    private JComboBox textAnswer;
    private JTable tableAnswer;

    private ImageIcon img;

    private ArrayList<String> wordsStore; //список всех ответов
    private ArrayList<Integer> timeAnswer; //список ответов для раунда
    private ArrayList<Integer> removeAnswer; //список ответов для раунда

    private int countNumber = 0;
    private int countNumberOne = 0;
    private int roundCount = 0;
    private int numDrop = 1;

    private Drop[] drop;

    private static final int MAXICON = 146;
    private Random rnd;
    private int iconNumer;


    Field(String label){

        about = new JPanel();
        about.setPreferredSize(new Dimension(400,160));
        about.setLayout(null);

        JLabel linOne = new JLabel("Тренажёр для развития памяти");
        linOne.setBounds(5,10,380,40);

        JLabel linFour = new JLabel("Распространяется БЕСПЛАТНО");
        linFour.setBounds(5,70,380,40);

        JLabel linFive = new JLabel("<html>Программу разработал Беляевский Антон atoty@mail.ru");
        linFive.setBounds(5,120,380,40);

        about.add(linOne);
        about.add(linFour);
        about.add(linFive);


        JMenuBar jMB = new JMenuBar();

        JMenu jProga = new JMenu("ПРОГРАММА");
        JMenuItem jAbout = new JMenuItem("О программе");
        jAbout.addActionListener(e -> showMessageDialog(about, about));
        jProga.add(jAbout);

        jMB.add(jProga);

        setJMenuBar(jMB);

        timeAnswer = new ArrayList<>(); // инициализируем список ответов
        removeAnswer = new ArrayList<>(); // инициализируем список удаленных ответов
        wordsStore = new ArrayList();
        timeAnswer.removeAll(removeAnswer);

        new Thread(() -> {

    try(Scanner inList = new Scanner((Field.class.getResourceAsStream("list.txt")),"Cp1251")){
        while(inList.hasNext()) {
            String[]line = inList.nextLine().split(" ");
            Collections.addAll(wordsStore, line);
        }
    }

        }).start();

        setTitle(label);
        Dimension sizeField = new Dimension(900, 655);
        setSize(sizeField);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel gameZone = new JPanel();
        Dimension sizeGameZone = new Dimension(600, 4);
        gameZone.setPreferredSize(sizeGameZone);
        gameZone.setLayout(new FlowLayout());
        gameZone.setBackground(Color.white);
        gameZone.setBorder(BorderFactory.createLineBorder(Color.blue,1));

        JPanel info = new JPanel();
        info.setBackground(Color.white);
        info.setLayout(null);
        Dimension sizeInfo = new Dimension(350, 655);
        info.setPreferredSize(sizeInfo);


        SpinnerNumberModel jsnm = new SpinnerNumberModel();
        jsnm.setMinimum(1);
        jsnm.setMaximum(81);
        jsnm.setValue(1);
        levelNumber = new JSpinner(jsnm);

        JLabel level = new JLabel("Уровень: ");
        level.setBounds(10,20,300,20);
        levelNumber.setBounds(70,20,35,20);

        JLabel speed = new JLabel("Скорость: ");
        speed.setBounds(120,20,300,20);
        JLabel sec = new JLabel("секунд");
        sec.setBounds(220,20,300,20);

        //УСТАНОВКА СКОРОСТИ
        SpinnerNumberModel sNumMod = new SpinnerNumberModel(1.0,0.5,5.0,0.5);
        speedNumber = new JSpinner(sNumMod);
        speedNumber.setBounds(180,20,35,20);

        //ВВОД ОТВЕТА-----------------------------------------------------------------------------------------------
        inAnswer = new JLabel("Список вариантов");
        inAnswer.setBounds(10,65,130,20);

        textAnswer = new JComboFilter(wordsStore);
        textAnswer.setEnabled(false);
        textAnswer.setBounds(135,62,140,26);


        for (String aWordsStore : wordsStore) textAnswer.addItem(aWordsStore);

        //КОЛИЧЕСТВО НАБРАННЫХ ОЧКОВ------------------------------------------------------

        round = new JLabel("Количество показов: " + roundCount);
        round.setBounds(10,100,300,100);

        pointsOne = new JLabel();
        pointsOne.setBounds(10,130,300,100);

        points = new JLabel();
        points.setBounds(10,160,300,100);

        //ПРОГРАММИРУЕМ КНОПКУ НАЧАТЬ-------------------------------------------------------------------------------
        ImageIcon startIco = new ImageIcon(Field.class.getResource("/start.png"));
        start = new JButton(startIco);
        start.setBounds(40,540,200,47);
        start.setBorder(BorderFactory.createEmptyBorder());
        start.setBackground(Color.white);
        start.addActionListener(new ActionListener() {
            private void run() {

                levelNumber.setEnabled(false);
                speedNumber.setEnabled(false);
                textAnswer.setEnabled(false);

                for(int i = 1; i < 82; i++) {
                    drop[i].setText(String.valueOf(i));
                    drop[i].setIcon(null);
                }

                timeAnswer.removeAll(timeAnswer); //очистить список с ответами
                numDrop = 1;
                inAnswer.setText("Укажи картинку № " + numDrop);

                for (int i = 0; i < 81; i++) {
                    tableAnswer.setValueAt("", i, 0);
                    tableAnswer.setValueAt("", i, 1);
                    tableAnswer.setValueAt("", i, 2);

                }

                int speedLevel = (int) ((double) speedNumber.getValue() * 1000);

                for (int i = 1; i <= (int) levelNumber.getValue(); i++) {

                    roundCount++;
                    round.setText("Количество показов: " + roundCount);

                    rnd = new Random();
                    iconNumer = 1 + rnd.nextInt(MAXICON);

                    timeAnswer.add(iconNumer); //заполняем список ответами
                    img = new ImageIcon(Field.class.getResource("/cover/" + iconNumer + ".png"));

                    drop[i].setIcon(img);
                    drop[i].setText("");
                    try {
                        Thread.sleep(speedLevel);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    drop[i].setIcon(null);
                    drop[i].setText(String.valueOf(i));
                }
                check.setEnabled(true);
                textAnswer.setEnabled(true);
                textAnswer.setSelectedItem("");
                textAnswer.requestFocus();


            }

            @Override
            public void actionPerformed(ActionEvent e) {

                countNumberOne = 0;
                start.setEnabled(false);

                new Thread((this::run)).start();

            }
        });

        ImageIcon checkIco = new ImageIcon(Field.class.getResource("/check.png"));
        check = new JButton(checkIco);
        check.setBounds(20,100,100,24);
        check.setBorder(BorderFactory.createEmptyBorder());
        check.setBackground(Color.white);
        check.setEnabled(false);

        //Прграммируем кнопку ПРОВЕРИТЬ---------------------------------------------------------------------------
        check.addActionListener(e -> new Thread(() -> {

            String word;

            switch ((timeAnswer.get(numDrop-1))-1){
                case 0: word = "автомат";
                    break;
                case 1: word = "ананас";
                    break;
                case 2: word = "бабочка";
                    break;
                case 3: word = "банан";
                    break;
                case 4: word = "барабан";
                    break;
                case 5: word = "батарейка";
                    break;
                case 6: word = "батон";
                    break;
                case 7: word = "башмак";
                    break;
                case 8: word = "бочка";
                    break;
                case 9: word = "валик";
                    break;
                case 10: word = "ведро";
                    break;
                case 11: word = "велосипед";
                    break;
                case 12: word = "вертолет";
                    break;
                case 13: word = "видеокамера";
                    break;
                case 14: word = "вилка";
                    break;
                case 15: word = "водопад";
                    break;
                case 16: word = "ворота";
                    break;
                case 17: word = "врач";
                    break;
                case 18: word = "галстук";
                    break;
                case 19: word = "гитара";
                    break;
                case 20: word = "гриб";
                    break;
                case 21: word = "грузовик";
                    break;
                case 22: word = "груша";
                    break;
                case 23: word = "дверь";
                    break;
                case 24: word = "дельфин";
                    break;
                case 25: word = "джин";
                    break;
                case 26: word = "диван";
                    break;
                case 27: word = "диск";
                    break;
                case 28: word = "дом";
                    break;
                case 29: word = "единорог";
                    break;
                case 30: word = "ёлка";
                    break;
                case 31: word = "жираф";
                    break;
                case 32: word = "жук";
                    break;
                case 33: word = "звезда";
                    break;
                case 34: word = "зонт";
                    break;
                case 35: word = "иголка";
                    break;
                case 36: word = "кактус";
                    break;
                case 37: word = "капля";
                    break;
                case 38: word = "карандаш";
                    break;
                case 39: word = "карусель";
                    break;
                case 40: word = "каска";
                    break;
                case 41: word = "кастрюля";
                    break;
                case 42: word = "кит";
                    break;
                case 43: word = "клоун";
                    break;
                case 44: word = "ключ";
                    break;
                case 45: word = "книги";
                    break;
                case 46: word = "колесо";
                    break;
                case 47: word = "кольцо";
                    break;
                case 48: word = "компас";
                    break;
                case 49: word = "конфета";
                    break;
                case 50: word = "конь";
                    break;
                case 51: word = "корабль";
                    break;
                case 52: word = "корона";
                    break;
                case 53: word = "космонавт";
                    break;
                case 54: word = "кость";
                    break;
                case 55: word = "краб";
                    break;
                case 56: word = "кран";
                    break;
                case 57: word = "креветка";
                    break;
                case 58: word = "кровать";
                    break;
                case 59: word = "кролик";
                    break;
                case 60: word = "крючок";
                    break;
                case 61: word = "кубок";
                    break;
                case 62: word = "кузнечик";
                    break;
                case 63: word = "курица";
                    break;
                case 64: word = "лампа";
                    break;
                case 65: word = "лампочка";
                    break;
                case 66: word = "ласты";
                    break;
                case 67: word = "лейка";
                    break;
                case 68: word = "лестница";
                    break;
                case 69: word = "лопата";
                    break;
                case 70: word = "лук";
                    break;
                case 71: word = "медаль";
                    break;
                case 72: word = "медведь";
                    break;
                case 73: word = "месяц";
                    break;
                case 74: word = "меч";
                    break;
                case 75: word = "микроскоп";
                    break;
                case 76: word = "микрофон";
                    break;
                case 77: word = "мишень";
                    break;
                case 78: word = "молния";
                    break;
                case 79: word = "молот";
                    break;
                case 80: word = "молоток";
                    break;
                case 81: word = "морковка";
                    break;
                case 82: word = "мороженое";
                    break;
                case 83: word = "мотоцикл";
                    break;
                case 84: word = "мяч";
                    break;
                case 85: word = "ножницы";
                    break;
                case 86: word = "носок";
                    break;
                case 87: word = "носорог";
                    break;
                case 88: word = "облако";
                    break;
                case 89: word = "орех";
                    break;
                case 90: word = "очки";
                    break;
                case 91: word = "палатка";
                    break;
                case 92: word = "пальма";
                    break;
                case 93: word = "перец";
                    break;
                case 94: word = "пила";
                    break;
                case 95: word = "пистолет";
                    break;
                case 96: word = "планета";
                    break;
                case 97: word = "поле";
                    break;
                case 98: word = "поплавок";
                    break;
                case 99: word = "пуговица";
                    break;
                case 100: word = "пушка";
                    break;
                case 101: word = "радуга";
                    break;
                case 102: word = "ракета";
                    break;
                case 103: word = "ракетка";
                    break;
                case 104: word = "рация";
                    break;
                case 105: word = "робот";
                    break;
                case 106: word = "рыба";
                    break;
                case 107: word = "рюкзак";
                    break;
                case 108: word = "светофор";
                    break;
                case 109: word = "свеча";
                    break;
                case 110: word = "сердце";
                    break;
                case 111: word = "скакалка";
                    break;
                case 112: word = "слон";
                    break;
                case 113: word = "снежинка";
                    break;
                case 114: word = "собака";
                    break;
                case 115: word = "сова";
                    break;
                case 116: word = "солнце";
                    break;
                case 117: word = "стрекоза";
                    break;
                case 118: word = "стул";
                    break;
                case 119: word = "сыр";
                    break;
                case 120: word = "такси";
                    break;
                case 121: word = "танк";
                    break;
                case 122: word = "телевизор";
                    break;
                case 123: word = "тележка";
                    break;
                case 124: word = "телефон";
                    break;
                case 125: word = "топор";
                    break;
                case 126: word = "трактор";
                    break;
                case 127: word = "трамвай";
                    break;
                case 128: word = "тыква";
                    break;
                case 129: word = "улитка";
                    break;
                case 130: word = "утка";
                    break;
                case 131: word = "фотоаппарат";
                    break;
                case 132: word = "футболка";
                    break;
                case 133: word = "цветок";
                    break;
                case 134: word = "чайник";
                    break;
                case 135: word = "часы";
                    break;
                case 136: word = "чашка";
                    break;
                case 137: word = "черепаха";
                    break;
                case 138: word = "шапка";
                    break;
                case 139: word = "шлем";
                    break;
                case 140: word = "шляпа";
                    break;
                case 141: word = "шорты";
                    break;
                case 142: word = "штанга";
                    break;
                case 143: word = "юбка";
                    break;
                case 144: word = "яблоко";
                    break;
                case 145: word = "якорь";
                    break;
                default: word = null;

            }

   if(Objects.equals(textAnswer.getSelectedItem(), word)) {
       tableAnswer.setValueAt("Да", numDrop - 1, 2);
       tableAnswer.setValueAt(word, numDrop - 1, 1);
       tableAnswer.setValueAt(numDrop, numDrop - 1, 0);

       drop[numDrop].setText("");
       ImageIcon imgAft = new ImageIcon(Field.class.getResource("/cover/" + timeAnswer.get(numDrop-1) + ".png"));
       drop[numDrop].setIcon(imgAft);

       countNumber++;
       countNumberOne++;
       numDrop++;
       textAnswer.setSelectedItem("");
       textAnswer.requestFocus();
   }
   else {
       tableAnswer.setValueAt("Нет", numDrop - 1, 2);
       tableAnswer.setValueAt(word, numDrop - 1, 1);
       tableAnswer.setValueAt(numDrop, numDrop - 1, 0);

       drop[numDrop].setText("");
       ImageIcon imgAft = new ImageIcon(Field.class.getResource("/cover/" + timeAnswer.get(numDrop-1) + ".png"));
       drop[numDrop].setIcon(imgAft);

       numDrop++;
       textAnswer.setSelectedItem("");
       textAnswer.requestFocus();
   }

   inAnswer.setText("Укажи картинку № " + numDrop);

   pointsOne.setText("Количество совпадений в раунде: " + countNumberOne);


   if(numDrop == (int) levelNumber.getValue()+1)
   {
       check.setEnabled(false);
       inAnswer.setText("Укажи картинку № " + (--numDrop));
       points.setText("Общее количество совпадений: " + countNumber);
       start.setEnabled(true);
       levelNumber.setEnabled(true);
       speedNumber.setEnabled(true);
   }

        }).start());

        info.add(level);
        info.add(levelNumber);
        info.add(speed);
        info.add(speedNumber);
        info.add(sec);
        info.add(inAnswer);
        info.add(textAnswer);
        info.add(pointsOne);
        info.add(points);
        info.add(pointsOne);
        info.add(round);
        info.add(start);
        info.add(check);

        drop = new Drop[82];

        for(int i = 1; i < 82; i++) {
            drop[i] = new Drop(String.valueOf(i));
            gameZone.add(drop[i]);
        }
        drop[1].setEnabled(true);

        levelNumber.addChangeListener(e -> {

            int kol = (int) levelNumber.getValue();

            for(int i = kol+1; i < 82; i++) {
                drop[i].setEnabled(false);
            }

            for(int i = 1; i < kol; i++) {
                if(i == 81) break;
                drop[i + 1].setEnabled(true);
            }

        });

        //ФОРМИРУЕМ ТАБЛИЦУ ОТВЕТОВ-----------------------------------------------------------------
        tableAnswer = new JTable(81,3);
        tableAnswer.setEnabled(false);
        tableAnswer.getColumn("A").setHeaderValue("№");
        tableAnswer.getColumn("B").setHeaderValue("ответы");
        tableAnswer.getColumn("C").setHeaderValue("совпадения");

        tableAnswer.getColumnModel().getColumn(0).setPreferredWidth(20);
        tableAnswer.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableAnswer.getColumnModel().getColumn(2).setPreferredWidth(80);
        tableAnswer.setPreferredScrollableViewportSize(new Dimension(265,300));

        JScrollPane answer = new JScrollPane(tableAnswer);
        answer.setBackground(Color.white);
        answer.setBorder(BorderFactory.createLineBorder(Color.gray,1));
        answer.setBounds(10,230,265,300);

        info.add(answer);

        add(gameZone,BorderLayout.WEST);
        add(info,BorderLayout.CENTER);
    }

}
