const app = new Vue({
    el: "#app",
    data: {
        files: [],//文件列表
        parentPath: "",//当前路径的父路径
        nowPath: "",//当前路径
        showDialog: false,//对话框
        selectedFile: null,//选择的文件
        info: "",//上传状态
        selectedItem: []
    },
    mounted: function () {
        this.fetchList(this.nowPath);
    },
    methods: {
        fetchList(path) {
            axios.get('/api/getFiles', {
                params: {
                    path: path
                }
            })
                .then(res => {
                    this.parentPath = res.headers['parent-path'];
                    this.nowPath = path;
                    this.files = res.data;
                })
                .catch(error => {
                    console.log(error);
                })

        },
        download(path, filename) {
            axios('/api/download', {
                params: {
                    path: path
                },
                responseType: 'blob'
            }).then(res => {
                const blob = new Blob([res.data], {type: res.headers['content-type']});
                const link = document.createElement('a');
                link.href = URL.createObjectURL(blob);
                link.download = filename;
                link.click();
            })
        },
        deleteFile(path) {
            axios('/api/delete', {
                params: {
                    path: path
                }
            }).then(res => {
                if (res) {
                    alert("SUCCESS");
                    this.fetchList(this.nowPath)
                } else {
                    alert("ERROR,PLEASE CHECK LOG");
                }
            })
        },
        handleFileChange(event) {
            this.selectedFile = event.target.files[0];
        },
        uploadFile() {
            if (this.selectedFile != null) {
                const formData = new FormData();
                formData.append('file', this.selectedFile);

                this.info = "Uploading....."

                axios.post('/api/upload', formData, {
                    params: {
                        path: this.nowPath
                    },
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                }).then(res => {
                    if (res.data === true) {
                        this.info = "OK!"
                        this.fetchList(this.nowPath)
                    } else {
                        this.info = "ERROR!"
                        console.log(res.data)
                    }
                }).catch(error => {
                    console.error(error);
                });
            } else {
                this.info = "File is null."
            }

        },
        toKb(size) {
            return parseFloat(size / 1024).toFixed(2);
        },
        toMb(size) {
            return parseFloat(size / (1024 * 1024)).toFixed(2);
        },
        deleteArray(files) {

            let data = [];
            this.selectedItem.forEach(function (item) {
                data.push(files[item])
            })
            if (data.length < 1) {
                Materialize.toast('You not selected items', 4000)
                return;
            }
            let json = JSON.stringify(data)
            let arr = JSON.parse(json)

            axios.post('/api/deleteFiles', {
                list: arr,
            }, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(function (res) {
                    if (res.data.status === "OK") {
                        Materialize.toast('Delete completed!', 4000)
                        this.selectedItem = []
                    } else {
                        Materialize.toast('Delete failed!Check log in console', 4000)
                        console.log(res.data.info)
                    }
                    this.fetchList(this.nowPath)

                }.bind(this))
                .catch(function (error) {
                    console.log(error);
                });

        }
    }


});