<template>
    <div class="login-body">
        <div class="login-container">
            <div class="login-head">
                <div class="login-name">
                    <div class="login-title">xxx-mananger</div>
                </div>
            </div>
            <a-form
                :model="formState"
                ref="formRef"
                :rules="rules"
                class="login-box"
            >
                <a-form-item label="登录名称：" name="username">
                    <a-input
                        v-model:value="formState.username"
                        autocomplete="off"
                    />
                </a-form-item>
                <a-form-item label="登录密码：" name="password">
                    <a-input
                        v-model:value="formState.password"
                        type="password"
                        autocomplete="off"
                    />
                </a-form-item>
                <a-form-item class="login-btn">
                    <a-button
                        type="primary"
                        html-type="submit"
                        @click="login"
                        v-model:loading="loading"
                        >登录</a-button
                    >
                    <a-button
                        style="margin-left: 10px"
                        @click="resetForm"
                        v-model:disabled="loading"
                        >重置</a-button
                    >
                </a-form-item>
            </a-form>
        </div>
    </div>
</template>

<script lang="ts">
import {
    RuleObject,
    ValidateErrorEntity,
} from 'ant-design-vue/es/form/interface';
import { defineComponent, reactive, ref, UnwrapRef, toRaw } from "vue";
import { mapState } from "vuex";
import { RootModule } from "../store/modules/root";
import { store } from "../store";
import * as action from "../store/root/actions";

interface FormState {
    username: string;
    password: string;
}

export default defineComponent({
    setup() {
        const formRef = ref();
        const formState: UnwrapRef<FormState> = reactive({
            username: "",
            password: "",
        });
        const resetForm = () => {
            formRef.value.resetFields();
        };
        const login = () => {
            console.log(store);
            formRef.value
                .validate()
                .then(() => {
                    RootModule.login(toRaw(formState));
                })
                .catch((error: ValidateErrorEntity<FormState>) => {
                    console.log("error", error);
                });
        };
        let validateUsername = async (rule: RuleObject, value: string) => {
            if (value == "") {
                return Promise.reject("请输入用户名");
            } else {
                Promise.resolve();
            }
        };
        let validatePassword = async (rule: RuleObject, value: string) => {
            if (value == "") {
                return Promise.reject("请输入密码");
            } else {
                Promise.resolve();
            }
        };
        const rules = {
            username: [{ validator: validateUsername, triger: "change" }],
            password: [{ validator: validatePassword, triger: "change" }],
        };
        return {
            formState,
            formRef,
            resetForm,
            login,
            rules,
        };
    },
    computed: {
        ...mapState("root", {
            loading: (state) => state.loading,
        }),
    },
});
</script>
<style scoped>
.login-body {
    width: 350px;
    margin: 150px auto;
    border: 1px solid #dcdfe6;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 0 30px #dcdfe6;
}
.login-title {
    text-align: center;
    margin: 10px;
    font-size: 1.7em;
}
.login-box {
    display: table;
    margin: 0 auto;
}
.login-btn {
    display: table;
    margin: 0 auto;
}
</style>