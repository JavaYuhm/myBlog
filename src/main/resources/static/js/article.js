const createButton = document.getElementById('create-btn')

if(createButton){
    createButton.addEventListener('click', event => {
            fetch(`/api/articles/`, {
                method: 'POST',
                headers: {
                    "Content-Type":"application/json",
                },
                body: JSON.stringify({
                    title: document.getElementById('title').value,
                    content: document.getElementById('content').value
                })
            })
                .then(()=> {
                    alert('등록 완료되었습니다');
                    location.replace('/articles');
                })

    })
}


const deleteButton = document.getElementById('delete-btn')

if(deleteButton){
    deleteButton.addEventListener('click', event => {
        if(confirm("삭제 하시겠습니까?")){
            let id = document.getElementById('article-id').value;
            fetch(`/api/articles/${id}`, {
                method: 'DELETE'
            })
                .then(()=> {
                    alert('삭제가 완료되었습니다');
                    location.replace('/articles');
                })
        }
    })
}
// fetch 메서드를 통해 DELETE 요청을 보냄.
// then() fetch 메서드가 정상인 경우, 실행

const modifyButton = document.getElementById('modify-btn')

if(modifyButton){
    modifyButton.addEventListener('click', event => {
        if(confirm("수정 하시겠습니까?")){
            let params = new URLSearchParams(location.search);
            let id = params.get('id');
            fetch(`/api/articles/${id}`, {
                method: 'PUT',
                headers: {
                    "Content-Type":"application/json",
                },
                body: JSON.stringify({
                    title: document.getElementById('title').value,
                    content: document.getElementById('content').value
                })
            })
                .then(()=> {
                    alert('수정 완료되었습니다');
                    location.replace(`/articles/${id}`);
                })
        }
    })
}

