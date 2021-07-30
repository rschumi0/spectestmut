contract while_loop{
    function whileTest(int a) public returns (int) {
        int result = 0;
        int index = 0;

        while(index <= a){
            result = result + index;
            index = index + 1;
        }

        return result;
    }
}