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
     * Добавляет элемент в конец листа
     *
     * @param element элемент который нужно добавить
     */
    public void add(E element) {
        if (size == elements.length) {
            increaseCapacity();
        }
        elements[size++] = element;
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
        if (size == elements.length) {
            increaseCapacity();
        }
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    /**
     * Добавляет все элементы из коллекции в конец листа
     *
     * @param collection коллекция, содержащая элементы для добавления в лист
     */
    public void addAll(Collection<? extends E> collection) {
        for (E element : collection) {
            add(element);
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
            return (E) elements[index];
        }
        return null;
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
            return null;
        }
        E removedElement = (E) elements[index];
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        size--;
        return removedElement;
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
            if (o.equals(elements[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Удваивает размер массива, используемого для хранения элементов в листе
     */
    private void increaseCapacity() {
        Object[] newElements = new Object[elements.length * 2];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }

    /**
     * Удаляет все элементы из этого списка. Лист станет пустым после выполнения
     */
    public void clear() {
        elements = new Object[10];
        size = 0;
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
     * @param c Компаратор, используемый для сравнения элементов списка
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
        if (low < high) {
            int pivotIndex = partition(low, high, c);
            quicksort(low, pivotIndex - 1, c);
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
        E pivot = (E) elements[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (c.compare((E) elements[j], pivot) < 0) {
                i++; // Сдвиг указателя

                // Обмен элементов
                E temp = (E) elements[i];
                elements[i] = elements[j];
                elements[j] = temp;
            }
        }

        E temp = (E) elements[i + 1];
        elements[i + 1] = elements[high];
        elements[high] = temp;

        return i + 1;
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
        if (low < high) {
            int middle = low + (high - low) / 2;
            mergesort(low, middle, c, aux);
            mergesort(middle + 1, high, c, aux);
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
        for (int i = low; i <= high; i++) {
            aux[i] = (E) elements[i];
        }
        int i = low, j = middle + 1, k = low;

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
        while (i <= middle) {
            elements[k] = aux[i];
            k++;
            i++;
        }
    }

    @Override
    public String toString() {
        return "MyDynamicArray{" +
                "elements=" + Arrays.toString(elements) +
                ", size=" + size +
                '}';
    }
}

