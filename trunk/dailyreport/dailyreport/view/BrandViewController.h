//
//  BrandViewController.h
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import <Three20UI/Three20UI.h>
#import "BrandModel.h"
@interface BrandViewController : TTViewController<BrandDelegate>{
    NSString* _brand;
    BrandModel* _brandModel;
    
}
@property (nonatomic, copy) NSString *brand;
@property (nonatomic, retain) BrandModel *brandModel;
-(id) initWithBrand:(NSString*) brand;
@end
