//
//  Constants.m
//  appletvserver
//
//  Created by zhang wei on 13-3-24.
//
//
#import "Constants.h"
@implementation Constants

static NSArray* qqChannelArray = nil;

+ (NSArray*) qqChannelArray{
    if(!qqChannelArray){
        qqChannelArray= [NSArray arrayWithObjects:
                         [NSNumber numberWithInteger:QQ_Recommand],
                         [NSNumber numberWithInteger:QQ_Search],
                         [NSNumber numberWithInteger:QQ_DianShiJu],
                         [NSNumber numberWithInteger:QQ_DianYing],
                         [NSNumber numberWithInteger:QQ_DongMan],
                         [NSNumber numberWithInteger:QQ_ZongYi],
                         [NSNumber numberWithInteger:QQ_XinWen],
                         [NSNumber numberWithInteger:QQ_DianShiZhiBo],
                         [NSNumber numberWithInteger:QQ_MeiJu],
                         [NSNumber numberWithInteger:QQ_YuLe],
                         [NSNumber numberWithInteger:QQ_WeiJiangTang],
                         [NSNumber numberWithInteger:QQ_TiYu],
                         [NSNumber numberWithInteger:QQ_JiLuPian],
                         nil];
  
    }
    return qqChannelArray;
}


+ (NSString*) qqChannelConvertToString:(QQChannel) channel {
    NSString *result = nil;
    
    switch(channel) {
        case QQ_Recommand:
            result = @"推荐";
            break;
        case QQ_Search:
            result = @"搜索";
            break;
        case QQ_DianShiJu:
            result = @"电视剧";
            break;
        case QQ_DianYing:
            result = @"电影";
            break;
        case QQ_DongMan:
            result = @"动漫";
            break;
        case QQ_ZongYi:
            result = @"综艺";
            break;
        case QQ_XinWen:
            result = @"新闻";
            break;
        case QQ_DianShiZhiBo:
            result = @"电视直播";
            break;
        case QQ_MeiJu:
            result = @"美剧";
            break;
        case QQ_YuLe:
            result = @"娱乐";
            break;
        case QQ_WeiJiangTang:
            result = @"微讲堂";
            break;
        case QQ_TiYu:
            result = @"体育";
            break;
        case QQ_JiLuPian:
            result = @"纪录片";
            break;
        default:
            result = @"其他";
    }
    
    return result;
}
@end
