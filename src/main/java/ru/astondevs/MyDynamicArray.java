package ru.astondevs;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

/**
 * Кастомная реализация ArrayList.
 *
 * @param <E> тип элемента
 */
public class MyDynamicArray<E> {
    private Object[] elements;
    private int size;

    /**
     * Конструктор инициализирует лист размером в 10 элементов
     */
    public MyDynamicArray() {
        elements = new Object[10];
    }

    /**
     * Добавляет элемент в конец листа.
     *
     * @param element элемент который нужно добавить
     */
    public void add(E element) {
        if (size == elements.length) { // Проверка, нужно ли увеличить размер массива
            increaseCapacity();
        }
        elements[size++] = element; // Добавление элемента и увеличение счетчика size
    }

    /**
     * Вставляет элемент в указанную позицию в листе
     * Если в нужной позиции уже есть элемент, сдвигаем этот элемент
     * и все последующие элементы вправо
     *
     * @param index   индекс, в который нужно вставить элемент
     * @param element элемент для вставки
     */
    public void add(int index, E element) {
        if (size == elements.length) { // Проверка, нужно ли увеличить размер массива
            increaseCapacity();
        }
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1]; // Сдвиг элементов вправо
        }
        elements[index] = element; // Вставка нового элемента
        size++; // Увеличение размера
    }

    /**
     * Добавляет все элементы из коллекции в конец листа
     *
     * @param collection коллекция, содержащая элементы для добавления в лист
     */
    public void addAll(Collection<? extends E> collection) {
        for (E element : collection) {
            add(element); // Добавление каждого элемента коллекции
        }
    }

    /**
     * Возвращает элемент, который находится на указанной позиции в листе
     *
     * @param index индекс элемента, который нужно вернуть
     * @return элемент, находящийся на указанной позиции
     */
    public E get(int index) {
        if (index < size) {
            return (E) elements[index]; // Возврат элемента, если индекс в пределах размера массива
        }
        return null; // Возврат null, если индекс вне диапазона
    }

    /**
     * Удаляет элемент, находящийся на указанной позиции в этом листе
     * Сдвигает все последующие элементы влево
     *
     * @param index индекс элемента, который нужно удалить
     * @return элемент, который был удален
     */
    public E remove(int index) {
        if (index >= size) {
            return null; // Возврат null, если индекс вне диапазона
        }
        E removedElement = (E) elements[index]; // Сохранение удаляемого элемента
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1]; // Сдвиг элементов влево
        }
        size--; // Уменьшение размера
        return removedElement; // Возврат удаленного элемента
    }

    /**
     * Ищет элемент который равен переданному, если элемент найден
     * метод его удаляет, если не найден ничего не происходит
     *
     * @param o элемент, который нужно удалить
     * @return true, если этот список содержал указанный элемент
     */
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) { // Проверка на равенство
                remove(i); // Удаление элемента
                return true; // Возврат true, элемент найден и удален
            }
        }
        return false; // Возврат false, элемент не найден
    }

    /**
     * Удваивает размер массива, используемого для хранения элементов в листе
     */
    private void increaseCapacity() {
        Object[] newElements = new Object[elements.length * 2]; // Создание нового массива в два раза больше
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i]; // Копирование элементов в новый массив
        }
        elements = newElements; // Обновление ссылки на массив
    }

    /**
     * Удаляет все элементы из этого списка. Лист станет пустым после выполнения
     */
    public void clear() {
        elements = new Object[10]; // Переинициализация массива
        size = 0; // Обнуление размера
    }

    /**
     * Возвращает true, если этот лист не содержит элементов
     *
     * @return true, если этот список не содержит элементов
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Сортирует лист с использованием компаратора. Использует быструю сортировку
     * для листа размером до 50 элементов и сортировку слиянием для более объемных листов
     *
     * @param c Компаратор, используемый для сравнения элементов списка.
     */
    public void sort(Comparator<? super E> c) {
        if (size <= 50) {
            E[] aux = (E[]) new Object[size];
            mergesort(0, size - 1, c, aux);
        } else {
            quicksort(0, size - 1, c);
        }
    }

    /**
     * Метод для реализации быстрой сортировки
     *
     * @param low  нижний индекс диапазона для сортировки
     * @param high иерхний индекс диапазона для сортировки
     * @param c    компаратор используемый для сравнения элементов
     */
    private void quicksort(int low, int high, Comparator<? super E> c) {
        if (low < high) { // Проверка, есть ли что сортировать
            // Вычисление индекса опорного элемента
            int pivotIndex = partition(low, high, c);
            // Рекурсивный вызов для левой части массива
            quicksort(low, pivotIndex - 1, c);
            // Рекурсивный вызов для правой части массива
            quicksort(pivotIndex + 1, high, c);
        }
    }

    /**
     * Метод для разделения элементов в рамках быстрой сортировки
     *
     * @param low  нижний индекс диапазона для разделения
     * @param high верхний индекс диапазона для разделения
     * @param c    компаратор используемый для сравнения элементов
     * @return индекс опорного элемента после разделения
     */
    private int partition(int low, int high, Comparator<? super E> c) {
        E pivot = (E) elements[high]; // Выбор опорного элемента
        int i = low - 1; // Инициализация указателя для меньшего элемента

        for (int j = low; j < high; j++) { // Перебор элементов
            if (c.compare((E) elements[j], pivot) < 0) { // Сравнение с опорным элементом
                i++; // Сдвиг указателя

                // Обмен элементов
                E temp = (E) elements[i];
                elements[i] = elements[j];
                elements[j] = temp;
            }
        }

        // Поместить опорный элемент между левой и правой частями массива
        E temp = (E) elements[i + 1];
        elements[i + 1] = elements[high];
        elements[high] = temp;

        return i + 1; // Возврат позиции опорного элемента
    }


    /**
     * Метод для реализации сортировки слиянием
     *
     * @param low  нижний индекс диапазона для сортировки
     * @param high иерхний индекс диапазона для сортировки
     * @param c    компаратор используемый для сравнения элементов
     * @param aux  вспомогательный массив для слияния
     */
    private void mergesort(int low, int high, Comparator<? super E> c, E[] aux) {
        if (low < high) { // Проверка, есть ли что сортировать
            int middle = low + (high - low) / 2; // Нахождение середины диапазона
            // Рекурсивный вызов для левой части массива
            mergesort(low, middle, c, aux);
            // Рекурсивный вызов для правой части массива
            mergesort(middle + 1, high, c, aux);
            // Слияние двух отсортированных подмассивов
            merge(low, middle, high, c, aux);
        }
    }

    /**
     * Метод для слияния двух отсортированных подмассивов
     *
     * @param low    начальный индекс первого подмассива
     * @param middle конечный индекс первого подмассива
     * @param high   конечный индекс второго подмассива
     * @param c      компаратор используемый для сравнения элементов
     * @param aux    вспомогательный массив для слияния
     */
    private void merge(int low, int middle, int high, Comparator<? super E> c, E[] aux) {
        // Копирование элементов в вспомогательный массив
        for (int i = low; i <= high; i++) {
            aux[i] = (E) elements[i];
        }

        int i = low, j = middle + 1, k = low; // Инициализация указателей

        // Перемещение меньшего элемента из двух подмассивов обратно в основной массив
        while (i <= middle && j <= high) {
            if (c.compare(aux[i], aux[j]) <= 0) {
                elements[k] = aux[i];
                i++;
            } else {
                elements[k] = aux[j];
                j++;
            }
            k++;
        }

        // Копирование оставшихся элементов из левой части, если таковые имеются
        while (i <= middle) {
            elements[k] = aux[i];
            k++;
            i++;
        }
        // Элементы из правой части копировать не нужно, так как они уже находятся на своих местах
    }

    @Override
    public String toString() {
        return "MyDynamicArray{" +
                "elements=" + Arrays.toString(elements) +
                ", size=" + size +
                '}';
    }
}

