//
//  IndexListModel.h
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Three20Network/Three20Network.h>
#import "BaseModel.h"
@interface IndexListModel : BaseModel{
    NSMutableArray* _brandList;
    BOOL _yesterday;
    NSDate* _date;
}
@property (nonatomic, retain) NSMutableArray* brandList;
@property (nonatomic, assign) BOOL yesterday;
@property (nonatomic, retain) NSDate* date;
@end
