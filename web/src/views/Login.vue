<template>
    <div class="login-body">
        <div class="login-head">
            <div class="login-name">
                <div class="login-title">Login</div>
            </div>
        </div>
        <a-form
            :model="formState"
            ref="formRef"
            :rules="rules"
            class="login-box"
        >
            <a-form-item name="username">
                <a-input
                    v-model:value="formState.username"
                    placeholder="Username"
                />
                <template #prefix>
                    <user-outlined type="user" />
                </template>
            </a-form-item>
            <a-form-item name="password">
                <a-input-password
                    v-model:value="formState.password"
                    placeholder="Password"
                />
                <template #prefix>
                    <UnlockOutlined />
                </template>
            </a-form-item>
            <a-button
                type="primary"
                html-type="submit"
                @click="login"
                v-model:loading="loading"
                >登录</a-button
            >
        </a-form>
    </div>
</template>

<script lang="ts">
import {
    RuleObject,
    ValidateErrorEntity,
} from "ant-design-vue/es/form/interface";
import { UserOutlined, UnlockOutlined } from "@ant-design/icons-vue";
import { defineComponent, reactive, ref, UnwrapRef, toRaw } from "vue";
import { mapState } from "vuex";
import { RootModule } from "/@/store/modules/root";

interface FormState {
    username: string;
    password: string;
}

export default defineComponent({
    components: {
        UserOutlined,
        UnlockOutlined,
    },
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
            loading: (state: any) => state.loading,
        }),
    },
});
</script>
<style lang='less' scoped>
.login-body {
    width: 500px;
    margin: 150px auto;
    border: 1px solid #dcdfe6;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 0 30px #dcdfe6;
    display: flex;
    flex-direction: column;
    align-items: center;

    .login-title {
        text-align: center;
        margin: 10px;
        font-size: 1.7em;
        margin-bottom: 30px;
    }

    .login-box {
        display: block;
        margin: 0 auto;
        margin-bottom: 30px;

        .ant-form-item {
            width: 400px;
            display: block;
        }

        .ant-btn{
            width: 400px;
        }
    }
}
</style>