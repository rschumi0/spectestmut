contract array_fixed_size_1d{
    uint[3] array;

    constructor() public{
        array[0] = 1;
        array[1] = 2;
        array[2] = 3;
    }

    function arrayRead(uint index) public returns (uint) {
        if(index < 3){
            return array[index];
        }
        else{
            return array[0];
        }
    }
}