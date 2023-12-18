import ru.astondevs.MyDynamicArray;

import java.util.Comparator;

public class MainClass {
    public static void main(String[] args) {
        MyDynamicArray<String> list = new MyDynamicArray<>();

        list.add("Яблоко");
        list.add("Апельсин");
        list.add("Банан");
        list.add("Виноград");
        list.add("Ананас");
        list.add("Вишня");
        list.add("Манго");
        list.add("Клубника");
        list.add("Голубика");
        list.add("Малина");
        list.add("Ежевика");
        list.add("Киви");
        list.add("Груша");
        list.add("Персик");

        System.out.println("До сортировки: " + list);

        list.sort(Comparator.naturalOrder());

        System.out.println("После сортировки: " + list);

        list.add(2, "Лимон");
        System.out.println("После вставки Лимона на позицию 2: " + list);

        list.remove(1);
        System.out.println("После удаления элемента на позиции 1: " + list);

        list.remove("Персик");
        System.out.println("После удаления Персика: " + list);

        String fruit = list.get(3);
        System.out.println("Элемент на позиции 3: " + fruit);

        System.out.println("Пуст ли список? " + list.isEmpty());

        list.clear();
        System.out.println("После очистки списка: " + list);

        System.out.println("Пуст ли список после очистки? " + list.isEmpty());
    }
}