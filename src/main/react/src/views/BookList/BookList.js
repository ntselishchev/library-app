import React, { Component } from 'react';
import {Button, Table, Row, Col} from "antd";
import ActionButtons from "../../Components/ActionButtons";
import Utils from "../../Utils/Utils";
import AddModal from "../../Components/AddModal";

class BookList extends Component {

    state = {
        data: undefined,
        addActive: false
    };

    columns = [
        {
            title: 'Book Id',
            dataIndex: 'id'
        },
        {
            title: 'Title',
            dataIndex: 'title'
        },
        {
            title: 'Author',
            dataIndex: 'author.name'
        },
        {
            title: 'Genre',
            dataIndex: 'genre.title'
        },
        {
            title: 'Action',
            dataIndex: 'action',
            render: (i, row) => {
                return (
                    <ActionButtons data={row} refreshView={this.refreshView.bind(this)}/>
                )
            }
        },
    ];

    componentWillMount() {
        this.requestData();
    }

    requestData() {
        Utils.getData("get-all").then((rs) => {
            this.setState({data: rs ? rs : []})
        });
    }

    refreshView() {
        this.requestData();
    }

    handleAdd = () => {
        const {addActive} = this.state;

        this.setState({
            addActive: !addActive
        });
    };

    handleCreate = (edited) => {
        const {addActive} = this.state;

        if (edited) {
           this.requestData();
        }
        this.setState({
            addActive: !addActive
        });
    };

    render() {
        const {data, addActive} = this.state;

        return (
            <React.Fragment>
                {addActive &&
                    <AddModal completed={this.handleCreate}/>
                }
                <Row>
                    <Col>
                        <h2 style={{textAlign: 'center'}}>BOOKS</h2>
                    </Col>
                </Row>
                <Row>
                    <Col>
                        <Table
                            rowKey={"id"}
                            dataSource={data}
                            columns={this.columns}
                            pagination={false}
                        />
                    </Col>
                </Row>
                <Row style={{marginTop: '10px', marginLeft: '10px'}}>
                    <Col>
                        <Button onClick={this.handleAdd} type="primary" style={{ marginBottom: 16 }}>
                            Add a row
                        </Button>
                    </Col>
                </Row>
            </React.Fragment>
        )
    }
}

export default BookList;
