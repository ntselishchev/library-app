import React, { Component } from 'react';
import {Select, Row, Col, Input, Modal, Button, Icon} from "antd";
import Utils from "../Utils/Utils";
const { Option } = Select;

class AddModal extends Component {

    state = {};

    componentWillMount() {
        this.getAuthorsAndGenres();
    }

    getAuthorsAndGenres() {
        this.getAuthors();
        this.getGenres();
    }

    getAuthors() {
        Utils.getData("authors").then((rs) => {
            this.setState({authors: rs ? rs : []})
        });
    }

    getGenres() {
        Utils.getData("genres").then((rs) => {
            this.setState({genres: rs ? rs : []})
        });
    }

    handleOk = () => {
        const {completed} = this.props;
        const {val, selectedAuthor, selectedGenre} = this.state;

        Utils.postData({
            title: val,
            authorId: selectedAuthor,
            genreId: selectedGenre
        }).then((rs) => {
            if (rs === 200) {
                completed(true);
            }
        });

    };

    handleCancel = () => {
        const {completed} = this.props;
        completed();
    };

    updateInputValue(evt) {
        this.setState({
            val: evt.target.value
        });
    }

    changeAuthor = (author) => {
        this.setState({selectedAuthor: author})
    };

    changeGenre = (genre) => {
        this.setState({selectedGenre: genre})
    };

    allPropertiesSelected = () => {
        const {selectedAuthor, selectedGenre, val} = this.state;

        return val && val.length > 0 && selectedAuthor && selectedGenre
    };

    render() {
        const {authors, genres, val} = this.state;

        return (
            <Modal
                title={'Add Book'}
                visible={true}
                onOk={this.handleOk}
                onCancel={this.handleCancel}
                footer={[
                    <Button key="back"
                            style={{fontSize: '12px'}}
                            onClick={this.handleCancel}>
                        <Icon type="rollback" />
                        Cancel
                    </Button>,
                    <Button key="apply"
                            type="primary"
                            style={{fontSize: '12px'}}
                            onClick={this.handleOk}
                            disabled={!this.allPropertiesSelected()}>
                        <Icon type="check-circle-o" />
                        Apply
                    </Button>
                ]}
            >
                <Row >
                    <Col span={10}>
                        <Input placeholder={'Type Title'}
                               value={val}
                               onChange={evt => this.updateInputValue(evt)}
                        />
                    </Col>
                    <Col span={6} offset={1}>
                        <Select placeholder={'Select Author'} style={{ width: 120 }} onChange={this.changeAuthor}>
                            {
                                authors && authors.length > 0 ?
                                    authors.map((content) => (
                                        <Option value={content.id} key={content.id}>
                                            {content.name}
                                        </Option>
                                    )) : undefined
                            }
                        </Select>
                    </Col>
                    <Col span={6} offset={1}>
                        <Select placeholder={'Select Author'} style={{ width: 120 }} onChange={this.changeGenre}>
                            {
                                genres && genres.length > 0 ?
                                    genres.map((content) => (
                                        <Option value={content.id} key={content.id}>
                                            {content.title}
                                        </Option>
                                    )) : undefined
                            }
                        </Select>
                    </Col>
                </Row>
            </Modal>
        )
    }
}

export default AddModal;
