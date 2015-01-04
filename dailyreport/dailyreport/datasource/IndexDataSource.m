//
//  IndexDataSource.m
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "IndexDataSource.h"
#import "Brand.h"
#import "IndexTableItem.h"
#import "IndexTableItemCell.h"
#import "AppDelegate.h"
@implementation IndexDataSource
@synthesize indexListModel=_indexListModel;

- (id)init{
    if ((self = [super init])) {
        self.indexListModel = [[[IndexListModel alloc] init] autorelease];
    }
    return self;
}

- (id<TTModel>)model {
    return _indexListModel;
}

- (void)tableViewDidLoadModel:(UITableView*)tableView {
    NSMutableArray* items = [[NSMutableArray alloc] init];
    int i = 0;
    for (Brand* brand in _indexListModel.brandList) {
        
        NSNumberFormatter* formatter = [[[NSNumberFormatter alloc]init] autorelease];
        formatter.numberStyle = NSNumberFormatterDecimalStyle;
        
        NSString* strValue = nil;
        if(brand.dayAmount.intValue>10000||brand.dayAmount.intValue==0){
            [formatter setMinimumFractionDigits:0];
            [formatter setMaximumFractionDigits:0];
            strValue=[formatter stringFromNumber:[NSNumber numberWithDouble:brand.dayAmount.doubleValue/10000]];
        }else{
            [formatter setMinimumFractionDigits:2];
            [formatter setMaximumFractionDigits:2];
            strValue=[formatter stringFromNumber:[NSNumber numberWithDouble:brand.dayAmount.doubleValue/10000]];
        }
        NSString* text = [NSString stringWithFormat:@"<span style=\"text-align:right\">%@</span>",strValue];
        
        TTStyledText* styledText = [TTStyledText textFromXHTML:text lineBreaks:YES URLs:YES];
        NSString* url = [NSString stringWithFormat:@"peacebird://brand/%@", [brand.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];

        NSString* imageName = [NSString stringWithFormat:@"首页%@",brand.brand];
        [items addObject:[IndexTableItem itemWithStyledText:styledText backgroundImage:imageName URL:url index:i brand:brand.brand]];
        i++;
    }
    if ([items count] == 0) {
        [items addObject:[TTTableTextItem itemWithText:@"没有可查看的品牌数据"]];
    }
    
    
    self.items = items;
    
    NSLog(@"count=%i",[self.items count]);
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    NSDateFormatter *todayDateFormatter = [[[NSDateFormatter alloc] init] autorelease];
    [todayDateFormatter setDateFormat:@"yyyy/MM/dd"];
    NSString* yesterdayStr = [todayDateFormatter stringFromDate:[[NSDate date] dateByAddingDays:-1]];
    NSString* selectDateStr = nil;
    if(DrAppDelegate.user.date==nil){
         selectDateStr = [todayDateFormatter stringFromDate:DrAppDelegate.user.date];
    }

    NSString* lastAlertDateStr = [defaults objectForKey:LAST_ALERT_DATE_STR];
    NSString* lastAlertVersionStr = [defaults objectForKey:LAST_ALERT_VERSION_STR];
    bool alertDate = false;
    bool alertVersion = false;
    if(lastAlertDateStr==nil||![lastAlertDateStr isEqualToString:yesterdayStr]){
        if(selectDateStr==nil||[selectDateStr isEqualToString:yesterdayStr])
        alertDate = true;
    }
    if(DrAppDelegate.latestClientVersion!=nil&&![DrAppDelegate.latestClientVersion isEqualToString:VERSION]){
        if(lastAlertVersionStr==nil||![lastAlertVersionStr isEqualToString:yesterdayStr]){
            alertVersion = true;
            [defaults setObject:DrAppDelegate.latestClientVersion forKey:LAST_ALERT_VERSION_STR];
        }
        [defaults setObject:yesterdayStr forKey:LAST_ALERT_VERSION_STR];
    }

    DrAppDelegate.user.date =_indexListModel.date;
    if(!_indexListModel.yesterday){
        [defaults setObject:yesterdayStr forKey:LAST_ALERT_DATE_STR];
        NSDateFormatter *dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
        [dateFormatter setDateFormat:@"MM月dd日 EEEE"];
        IndexViewController* index = (IndexViewController*)[TTNavigator navigator].visibleViewController;
        [index updateDate];
        if(alertDate){
            TTAlert([NSString stringWithFormat:@"昨日数据未生成,当前数据为%@数据",[dateFormatter stringFromDate:_indexListModel.date]]);
        }
        if(alertVersion){
            UIAlertView* alert = [[[UIAlertView alloc] initWithTitle:nil
                                                             message:@"PB-日报已有新版本,请更新"
                                                            delegate:self
                                                   cancelButtonTitle: @"更新"
                                                   otherButtonTitles:@"取消",nil] autorelease];

            [alert show];
        }
        
    }
    TT_RELEASE_SAFELY(items);
}
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(buttonIndex==0){
        TTOpenURL(@"https://itunes.apple.com/us/app/pb-ri-bao/id888870654");
    }else{
        [alertView dismissWithClickedButtonIndex:0 animated:YES];
    }
}
- (Class)tableView:(UITableView*)tableView cellClassForObject:(id) object {
    if ([object isKindOfClass:[IndexTableItem class]]) {
		return [IndexTableItemCell class];
	} else {
		return [super tableView:tableView cellClassForObject:object];
	}
	
}

///////////////////////////////////////////////////////////////////////////////////////////////////
- (NSString*)titleForLoading:(BOOL)reloading {
    return @"";
}

///////////////////////////////////////////////////////////////////////////////////////////////////
- (NSString*)titleForEmpty {
    return @"";
}


///////////////////////////////////////////////////////////////////////////////////////////////////
- (NSString*)subtitleForError:(NSError*)error {
    return @"加载品牌数据失败";
}
@end
