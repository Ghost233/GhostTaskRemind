//
// Created by Administrator on 2022/4/29.
//

#ifndef YSLOGSDK_JNIDEFER_H
#define YSLOGSDK_JNIDEFER_H

#include <vector>
#include <functional>

struct JNIDefer {
public:
    JNIDefer &operator+=(std::function<void()> block) {
        m_defers.emplace_back(std::move(block));
        return *this;
    }

    void JNIDeferRelease() {
        while (!m_defers.empty()) {
            if (m_defers.back()) m_defers.back()();
            m_defers.pop_back();
        }
    }

    ~JNIDefer() {
        JNIDeferRelease();
    }

private:
    std::vector <std::function<void()>> m_defers;
};

#define USE_DEFER JNIDefer __defer__
#define defer __defer__ += [=]
#define ReleaseDefer __defer__.JNIDeferRelease()

#endif //YSLOGSDK_JNIDEFER_H
