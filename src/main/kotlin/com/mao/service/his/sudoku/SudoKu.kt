package com.mao.service.his.sudoku

/**
 * 数独解析
 * 遍历数独
 * 有空值
 * 查找该空值可能性
 * 填上空值
 * 继续填下一空值
 * 可能性为为0，结束
 * 直到填空完毕
 *
 * 经典数独：
 * ╔═════╤═════╤═════╦═════╤═════╤═════╦═════╤═════╤═════╗
 * ║     │     │  5  ║  3  │     │     ║     │     │     ║
 * ╟─────┼─────┼─────╫─────┼─────┼─────╫─────┼─────┼─────╢
 * ║  8  │     │     ║     │     │     ║     │  2  │     ║
 * ╟─────┼─────┼─────╫─────┼─────┼─────╫─────┼─────┼─────╢
 * ║     │  7  │     ║     │  1  │     ║  5  │     │     ║
 * ╠═════╪═════╪═════╬═════╪═════╪═════╬═════╪═════╪═════╣
 * ║  4  │     │     ║     │     │  5  ║  3  │     │     ║
 * ╟─────┼─────┼─────╫─────┼─────┼─────╫─────┼─────┼─────╢
 * ║     │  1  │     ║     │  7  │     ║     │     │  6  ║
 * ╟─────┼─────┼─────╫─────┼─────┼─────╫─────┼─────┼─────╢
 * ║     │     │  3  ║  2  │     │     ║     │  8  │     ║
 * ╠═════╪═════╪═════╬═════╪═════╪═════╬═════╪═════╪═════╣
 * ║     │  6  │     ║  5  │     │     ║     │     │  9  ║
 * ╟─────┼─────┼─────╫─────┼─────┼─────╫─────┼─────┼─────╢
 * ║     │     │  4  ║     │     │     ║     │  3  │     ║
 * ╟─────┼─────┼─────╫─────┼─────┼─────╫─────┼─────┼─────╢
 * ║     │     │     ║     │     │  9  ║  7  │     │     ║
 * ╚═════╧═════╧═════╩═════╧═════╧═════╩═════╧═════╧═════╝
 *
 * create by mao at 2020/4/18 10:57
 */
class SudoKu {

    companion object {
        private const val MAX_SUCCESS = 5               //寻找到该值数量的结果后便停止寻找
    }

    val result: MutableList<Array<Array<Int>>>          //寻找到的结果集
    private var success: Int                            //当前寻找到的结果数量

    init {
        this.result = ArrayList()
        this.success = 0
    }

    /**
     * 对外提供的分析方法
     */
    fun analyse(src: Array<Array<Int>>?) {
        if (null != src)
            analyse(src,0,0)
    }

    /**
     * 解析主方法：
     * 如果此坐标[x,y]为0，表示需要填入数据
     *      先将数独克隆一份，使用克隆的数独进行操作（由于是各种可能性同时进行）
     *      获取该坐标可能的数据集合【标】
     *          如果集合为空：数组填到这说明填错了，该填充进程就此放弃
     *          如果集合不为空：说明还可以继续，遍历集合，让所有可能性继续走
     *              如果坐标为最后一位，说明填满了：此进程正确，放入map中
     *              如果坐标还有，继续作填下一个数的操作（递归）
     * 如果此坐标[x,y]不为0，表示这是数独中原有数据
     *      如果没到最后一个坐标，继续作填下一个数的操作（递归）
     *      如果到了最后一个坐标，那此进程正确，放入map
     *
     * 方法主要靠获取该坐标的可能数据集合进行判别
     * 而不需要在每次填入一个数据后，都要判断整个数独一行、一列、一宫有没有重复数字
     * 可以细细思量。
     * @param src 数独
     * @param x x横
     * @param y y竖
     */
    private fun analyse(src: Array<Array<Int>>, x: Int, y: Int) {
        if (success < MAX_SUCCESS)
            if (src[x][y] == 0) {
                val copy = copy(src)
                val probability =getProbability(copy,x,y)
                if (probability != null && probability.isNotEmpty()) {
                    for (i in probability) {
                        if (null != i){
                            copy[x][y] = i
                            val position = nextPosition(x, y)
                            if (null == position) {
                                result.add(copy)
                                success++
                            } else {
                                analyse(copy, position[0], position[1])
                            }
                        }
                    }
                }
            } else {
                val position = nextPosition(x,y)
                if (null == position) {
                    result.add(src)
                    success++
                } else {
                    analyse(src,position[0],position[1])
                }
            }
    }

    /**
     * 获取下一坐标
     * 8是最大值，以8作判断
     * @param x x横
     * @param y y竖
     * @return [x,y]
     */
    private fun nextPosition(x: Int, y: Int) : Array<Int>? {
        return if (x == 8) {
            if (y == 8) null
            else arrayOf(x, y+1)
        } else {
            if (y == 8) arrayOf(x+1, 0)
            else arrayOf(x, y+1)
        }
    }

    /**
     * 获取该坐标可以放置的值
     * 获取一横有的数
     * 获取一竖有的数
     * 获取一宫有的数
     * 统计3个数组获取1-9没有出现的数
     * @param src 源数独
     * @param x x横
     * @param y y竖
     * @return 可能值
     */
    private fun getProbability(src: Array<Array<Int>>, x: Int, y: Int) : Array<Int?>? {
        val count = arrayOf(arrayOf(1,0), arrayOf(2,0), arrayOf(3,0), arrayOf(4,0), arrayOf(5,0), arrayOf(6,0), arrayOf(7,0), arrayOf(8,0), arrayOf(9,0))
        for (i in 0..8)
            if (src[i][y] > 0)
                count[src[i][y] - 1][1]++
        for (j in 0..8)
            if (src[x][j] > 0)
                count[src[x][j] - 1][1]++
        for (i in getGongRange(x))
            for (j in getGongRange(y))
                if (src[i][j] > 0)
                    count[src[i][j] - 1][1]++
        val pre = arrayOfNulls<Int>(9)
        var c = 0
        for (ints in count)
            if (ints[1] == 0) {
                pre[c] = ints[0]
                c++
            }
        return pre
    }

    /**
     * 获取该数值所在宫的范围
     * 如：i=1  -> [0,1,2]
     */
    private fun getGongRange(i: Int) : Array<Int> {
        return if (i < 3) arrayOf(0,1,2) else if (i < 6) arrayOf(3,4,5) else arrayOf(6,7,8)
    }

    /**
     * 克隆
     */
    private fun copy(arr: Array<Array<Int>>) : Array<Array<Int>> {
        return arrayOf(
            arr[0].copyOf(),
            arr[1].copyOf(),
            arr[2].copyOf(),
            arr[3].copyOf(),
            arr[4].copyOf(),
            arr[5].copyOf(),
            arr[6].copyOf(),
            arr[7].copyOf(),
            arr[8].copyOf()
        )
    }

}