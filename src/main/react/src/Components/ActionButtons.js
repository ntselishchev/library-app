import React, { Component } from 'react';
import {Button} from "antd";
import EditModal from "./EditModal";
import Utils from "../Utils/Utils";

class BookList extends Component {

    state = {
        data: undefined,
        editActive: false
    };

    componentWillUpdate(nextProps, nextState, nextContext) {
        const {refreshView} = this.props;
        const {editActive} = this.state;
        if (editActive && !nextState.editActive && nextState.edited) {
            refreshView();
        }
    }

    handleEdit = (data, edited) => {
        const {editActive} = this.state;

        this.setState({
            editActive: !editActive,
            edited: edited,
            rowData: data
        });
    };

    handleDelete = () => {
        const {refreshView, data} = this.props;

        Utils.deleteData(
            {id: data.id}
            ).then((rs) => {
            if (rs === 200) {
                refreshView();
            }
        });
    };

    render() {
        const {editActive} = this.state;
        const {data} = this.props;

        return (
            <React.Fragment>
                {editActive &&
                    <EditModal data={data} completed={this.handleEdit}/>
                }
                <Button type="primary" onClick={this.handleEdit}>Edit</Button>
                <Button type="danger" style={{marginLeft: '10px'}} onClick={this.handleDelete}>Delete</Button>
            </React.Fragment>
        )
    }
}

export default BookList;
