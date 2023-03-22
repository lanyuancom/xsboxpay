/*
Copyright [2020] [https://www.xiaonuo.vip]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：

1.请不要删除和修改根目录下的LICENSE文件。
2.请不要删除和修改Snowy源码头部的版权声明。
3.请保留源码和相关描述文件的项目出处，作者声明等。
4.分发源码时候，请注明软件出处 https://gitee.com/xiaonuobase/snowy-layui
5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/xiaonuobase/snowy-layui
6.若您的项目无法满足以上几点，可申请商业授权，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.sys.modular;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vip.xiaonuo.core.context.login.LoginContextHolder;
import vip.xiaonuo.core.exception.enums.AuthExceptionEnum;
import vip.xiaonuo.core.pojo.login.SysLoginUser;
import vip.xiaonuo.core.util.FileDataUtil;
import vip.xiaonuo.core.util.HttpServletUtil;
import vip.xiaonuo.core.util.IpAddressUtil;
import vip.xiaonuo.sys.core.cache.RedisCache;
import vip.xiaonuo.sys.modular.auth.service.AuthService;
import vip.xiaonuo.sys.modular.msg.service.SysMessageService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 首页控制器
 *
 * @author xuyuxiang
 * @date 2020/3/18 11:20
 */
@Controller
public class IndexController {

    Log log = Log.get();

    @Resource
    private AuthService authService;

    @Resource
    private SysMessageService sysMessageService;

    @Resource
    RedisCache redisCache;
    /**
     * 访问首页，提示语
     *
     * @author xuyuxiang
     * @date 2020/4/8 19:27
     */
    @GetMapping("/")
    public String index(Model model) {
        //判断是否登录
        boolean hasLogin = LoginContextHolder.me().hasLogin();
        if(hasLogin) {
            SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUserUpToDate();
            model.addAttribute("loginUser", sysLoginUser);
            model.addAttribute("hasUnreadMsg", sysMessageService.hasUnreadMsg());
            return "index.html";
        } else {
            HttpServletRequest request = HttpServletUtil.getRequest();
            String token = authService.getTokenFromRequest(request);
            if(ObjectUtil.isNotNull(token)) {
                model.addAttribute("tips", AuthExceptionEnum.LOGIN_EXPIRED.getMessage());
            }
            String ip = IpAddressUtil.getIp(HttpServletUtil.getRequest());
            String str = FileDataUtil.readDataKey(ip,"xsbox_tag_login");
            log.info(" ip xsbox_tag_login -> "+str);
            if("1".equals(str)){
                return "redirect:/mgr/login";
            }else if("0".equals(str)){
                return "redirect:/mch/login";
            }else {
                return "login/login.html";
            }
        }
    }
}
