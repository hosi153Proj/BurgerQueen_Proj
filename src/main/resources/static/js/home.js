//home화면 자바스크립트 처리 : 23.10.10 지영
const buyBtn = document.getElementById("cartBtn");
const cols = document.querySelectorAll('#cartDiv .btn');


// var cartList = {};

//해당 사용자의 카트 정보를 자바스크립트 변수에 저장 (html소스 내)

//장바구니 버튼 클릭이벤트 처리(자바스크립트 장바구니에 저장)
[].forEach.call(cols, function(col){
    col.addEventListener("click" , click , false );
});

function click(e){
    //let productId = document.getElementById('productId').value;
    let productId = this.getAttribute("id")
    let productName = this.getAttribute("name")

    if(productId in cartList){
        certain('warning', "이미 장바구니에 있습니다.")
        // alert("이미 장바구니에 있습니다.");
    }
    else {
        cartListAll.push({productId : productId , quantity : 1})
        cartList[productId] = 1;
        console.log(cartListAll)
        certain('success',`${productName} 상품이 장바구니에 추가되었습니다.`);
        // window.alert(`${productName} 상품이 장바구니에 추가되었습니다.`);
    }
    // console.log(cartList)
}


//구매하기 버튼 클릭 시, cart post 및 페이지 이동
if(buyBtn){
    buyBtn.addEventListener('click',event =>{
        // let cartId = document.getElementById('cartId').value;
        console.log(`cartId: ${cartId}`)
        if(cartListAll.length === 0) location.href=`/empty-cart`;
        else {
            fetch(`/api/cart/${cartId}`, {
                method: 'PATCH',
                headers: {"Content-Type": "application/json",},
                body: JSON.stringify({
                    cartProductPatchDtos: cartListAll
                    //content: document.getElementById("editContent").value,
                    // member:document.getElementById("author").value

                })
            })
                .then((response) => {
                    if (response.ok) {
                        return response.json()
                    }
                    throw new Error(`Status: ${response.status} ! 요청 처리에 실패하였습니다 !`);
                }).then(data => {
                // sessionStorage.setItem('cart', cartListAll);
                location.href = `/cart`;
            })
                .catch(error => {
                    certain('warning', '잠시 후 시도해주세요.')
                })
        }
    })

}

function certain(icon, noti){
    Swal.fire({
        text: noti,
        icon: icon,
        showCancelButton: false,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '확인',
        cancelButtonText: '취소'
    }).then((result) => {
        if (result.value) {
            //"삭제" 버튼을 눌렀을 때 작업할 내용을 이곳에 넣어주면 된다.
        }
    })
}
