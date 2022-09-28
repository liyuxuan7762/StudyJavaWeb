function deleteFruit(fid) {
    if (confirm("确认删除？")) {
        window.location.href='fruit.do?fid='+fid+'&operType=delete';
    }
}

function queryPage(pageNo) {
    window.location.href = "fruit.do?pageNo=" + pageNo;
}