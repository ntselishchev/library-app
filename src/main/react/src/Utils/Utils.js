import axios from "axios";

const Utils = {

    getData(entityType) {
        return axios.get(entityType).then((response) => {
            return response.data
        })
    },

    putData(data) {
        return axios.put('books/' + data.id, data).then((response) => {
            return response.status
        })
    },

    deleteData(data) {
        return axios.delete('books/' + data.id, {data: {...data}}).then((response) => {
            return response.status
        })
    },

    postData(data) {
        return axios.post('books', data).then((response) => {
            return response.status
        })
    },

};

export default Utils;
