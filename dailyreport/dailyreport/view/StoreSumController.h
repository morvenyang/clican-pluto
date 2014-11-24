//
//  StoreSumController.h
//  dailyreport
//
//  Created by zhang wei on 14-11-24.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "SwitchViewController.h"
#import "StoreSumModel.h"
@interface StoreSumController : SwitchViewController<StoreSumDelegate>{
    StoreSumModel* _storeSumModel;
    NSMutableArray* _typeLabels;
    NSMutableArray* _tableViews;
    CGFloat _tableOffset;
    NSArray* _monthlySums;
    NSArray* _yearlySums;
}
@property (nonatomic, retain) StoreSumModel *storeSumModel;
@property (nonatomic, retain) NSMutableArray *typeLabels;
@property (nonatomic, retain) NSMutableArray* tableViews;
@property (nonatomic, retain) NSArray* monthlySums;
@property (nonatomic, retain) NSArray* yearlySums;
-(id) initWithBrand:(NSString*) brand;
@end
