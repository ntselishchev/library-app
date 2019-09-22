import axios from "axios";

const Utils = {

    getData(entityType) {
        return axios.get(entityType).then((response) => {
            if (response.status === 200 && response.request.responseURL === "http://localhost:8080/login") {
                this.redirectToHome()
            } else {
                return response.data
            }
        });
    },

    putData(data) {
        return axios.put('books/' + data.id, data).then((response) => {
            return response.status
        }).catch(() => this.redirectToHome());
    },

    deleteData(data) {
        return axios.delete('books/' + data.id, {data: {...data}}).then((response) => {
            return response.status
        }).catch(() => this.redirectToHome());
    },

    postData(data) {
        return axios.post('books', data).then((response) => {
            return response.status
        }).catch(() => this.redirectToHome());
    },

    redirectToHome(e) {
        console.log(e)
        document.location.href = 'http://localhost:8080/index.html'
    },

    logout() {
        return axios.put('logout').then((response) => {
            return response.status
        }).catch(() => this.redirectToHome());
    }

};

export default Utils;
