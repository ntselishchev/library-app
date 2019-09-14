import axios from "axios";

const Utils = {

    getData(entityType) {
        return axios.get('books/' + entityType).then((response) => {
            return response.data
        })
    },

    putData(data) {
        return axios.put('books/update', data).then((response) => {
            return response.status
        })
    },

    deleteData(data) {
        return axios.delete('books/delete', {data: {...data}}).then((response) => {
            return response.status
        })
    },

    postData(data) {
        return axios.post('books/create', data).then((response) => {
            return response.status
        })
    },

};

export default Utils;
